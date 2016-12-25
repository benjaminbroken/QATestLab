/**
 * Created by benjaminbroken on 25.12.2016.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Reporter.log;


public class BingTest {

    private WebDriver driver;
    private Actions actions;
    private JavascriptExecutor jse;

    @BeforeTest
    public void init() {
        String property = System.getProperty("user.dir") + "/driver/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", property);
        driver = new ChromeDriver();
        actions = new Actions(driver);
        jse = ((JavascriptExecutor) driver);
        Reporter.setEscapeHtml(false);
    }

    @Test(description = "open Bing main page")
    public void openBingMain() {
        log("Opening https://www.bing.com/ ...");
        driver.navigate().to("https://www.bing.com/");
        Assert.assertEquals(driver.getTitle(), "Bing", "Error while loading the page.");
        log("The page " + driver.getCurrentUrl() + " was loaded successfully.");
    }

    @Test(description = "navigate to image search", dependsOnMethods = "openBingMain")
    public void navigateToImageSearch() {
        log("Going to Bing Image Feed...");
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='scpl1']")));
        driver.findElement(By.xpath("//a[@id='scpl1']")).click();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.titleContains("Лента изображений Bing"));
        Assert.assertEquals(driver.getTitle(), "Лента изображений Bing", "Error while loading the page.");
        log("The page " + driver.getCurrentUrl() + " was loaded successfully.");
    }

    @Test(description = "scroll the page several times", dependsOnMethods = "navigateToImageSearch")
    public void scrollSeveralTimes() {
        log("Scrolling the page several times...");
        By showImages = By.xpath("//div[@class='img_cont hoff']/img");
        for (int i = 0; i < 3; i++) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            log("Waiting for image blocks to load for the " + i +" time...");
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(showImages));
        }
        log("The actions were performed successfully.");
    }

    @Test(description = "search by keywords", dependsOnMethods = "scrollSeveralTimes", dataProvider = "provideKeywords")
    public void searchByKeywords(ArrayList<String> keywords) {
        log("Seaching by the keywords provided in keywords.txt...");
        for (String word : keywords){
            log("Searching \"" + word + "\"..." );
            driver.findElement(By.className("b_searchbox")).clear();
            driver.findElement(By.className("b_searchbox")).sendKeys(new CharSequence[]{word, Keys.ENTER});
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("dg_u")));
            Assert.assertTrue(driver.getTitle().equals(word + " - Bing images"));
            log("Search results are shown for the \"" + word + "\" keyword. ");
        }
        log("The search was performed successfully.");
    }

    @Test(description = "check image frame", dependsOnMethods = "searchByKeywords")
    public void checkImageFrame() {
        log("Checking image frame and buttons...");
        WebElement element = driver.findElement(By.xpath("//div[1][@class='dg_u']"));
        log("Hovering the mouse to perform check...");
        actions.moveToElement(element).perform();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.className("irhc")));
        Assert.assertTrue(driver.findElement(By.className("irhc")).isDisplayed());
        log("The image frame and buttons are shown successfully.");

    }

    @Test(description = "check slideshow", dependsOnMethods = "checkImageFrame")
    public void searchByImage() {
        log("Searching by the image...");
        driver.findElement(By.xpath("//div[5]/div/span/img[1]")).click();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='iol_navr']")));
        Assert.assertTrue(driver.findElement(By.xpath("//*[@id='iol_navr']")).isDisplayed());
        log("The slideshow is loaded successfully.");
    }

    @Test(description = "count minimal number of images", dependsOnMethods = "searchByImage")
    @Parameters ("minNumber")
    public void clickMoreImagesButton(int minNumber) {
        log("Clicking the \"More images button\"...");
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.className("tab-head")));
        driver.findElement(By.id("mmComponent_images_4_1_1_exp")).click();
        int count = driver.findElements(By.xpath("//*[@id=\"mmComponent_images_4_1_1_list\"]/li")).size();
        Assert.assertTrue(count > minNumber);
        log("The images are shown.");
    }

    @DataProvider(name = "provideKeywords")
    public Object[][] provideKeywords() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/keywords/keywords.txt"));
        List<String> keywords = new ArrayList<String>();
        String keyword;

        while ((keyword = reader.readLine()) != null) {
            keywords.add(keyword);
        }

        reader.close();
        return new Object[][]{{keywords}};
    }

    @AfterTest
    public void endTest() {
        log("The test was finished.");
        driver.quit();
    }

}
