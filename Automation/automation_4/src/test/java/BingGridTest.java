/**
 * Created by benjaminbroken on 26.12.2016.
 */

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class BingGridTest {

    private WebDriver driver;

    @BeforeClass
    @Parameters("browser")
    public void setup(@Optional("chrome")final String browser) throws Exception {
        if (browser.equalsIgnoreCase("Chrome")) {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
        }
        if (browser.equalsIgnoreCase("Firefox")) {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox());
        }
        if (browser.equalsIgnoreCase("Android")) {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.android());
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to("https://www.bing.com/");
    }

    @Test
    public void checkElementsDisplay() {
        Assert.assertTrue(driver.findElement(By.className("hp_sw_logo")).isDisplayed() &&
                driver.findElement(By.className("b_searchbox")).isDisplayed() &&
                driver.findElement(By.className("b_searchboxSubmit")).isDisplayed());
    }

    @Test(dependsOnMethods = "checkElementsDisplay", dataProvider = "keyword")
    public void search(String keyword){
        driver.findElement(By.xpath("//input[@id='sb_form_q']")).sendKeys("automatio");
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.attributeToBe(By.xpath("//input[@id='sb_form_q']"), "aria-expanded", "true"));
        driver.findElement(By.xpath("//strong[text() = 'n']")).click();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.numberOfElementsToBe(By.xpath("//body/iframe"), 2));
    }

    @Test(dependsOnMethods = "search")
    public void saveNavCheck() {
        int numOfRes = driver.findElements(By.xpath("//*[@id='b_results']/li/div/h2/a")).size();
        final List<WebElement> results = driver.findElements(By.xpath("//*[@id='b_results']/li/div/h2/a"));
        for (WebElement element : results) {
            final String currentURL = element.getAttribute("href");
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id='b_results']/li/div/h2/a")));
            element.click();
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.urlToBe(currentURL));
            Assert.assertTrue(currentURL.equals(driver.getCurrentUrl()));
            driver.navigate().back();
        }
    }

    @DataProvider(name = "keyword")
    public final Object[][] testData() {
        return new Object[][]{{"automatio"}};
    }
}
