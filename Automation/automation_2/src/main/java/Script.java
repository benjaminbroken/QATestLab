/**
 * Created by benjaminbroken on 11.12.2016.
 * Generic script class can be used with any browser's web driver.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Script<T extends RemoteWebDriver> {

    private T driver;
    private WebDriverWait wait;

    public Script(T driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void script() {

        driver.navigate().to("https://www.bing.com/");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='scpl1']")));
        driver.findElement(By.xpath("//a[@id='scpl1']")).click();

        wait.until(ExpectedConditions.titleContains("Лента изображений Bing"));

        By showImages = By.xpath("//div[@class='img_cont hoff']/img");
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        for (int i = 0; i < 3; i++) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(showImages));
        }

        jse.executeScript("window.scrollTo(0, 0)");
        driver.findElement(By.xpath("//input[@id='sb_form_q']")).sendKeys("automatio");
        wait.until(ExpectedConditions.attributeToBe(By.xpath("//input[@id='sb_form_q']"), "aria-expanded", "true"));
        driver.findElement(By.xpath("//strong[text() = 'n']")).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//body/iframe"), 2));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text() = 'Дата']")));
        driver.findElement(By.xpath("//span[text() = 'Дата']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text() = 'В прошлом месяце']")));
        driver.findElement(By.xpath("//span[text() = 'В прошлом месяце']")).click();

        driver.findElement(By.xpath("//div[@class ='dg_u'][1]")).click();

        wait.until(ExpectedConditions.attributeToBe(By.xpath("//html[@style]"),"style","overflow: hidden;"));
        driver.switchTo().frame("OverlayIFrame");
        waitForSlideshow();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='iol_navr']")));
        driver.findElement(By.xpath("//a[@id='iol_navr']")).click();
        waitForSlideshow();

        wait.until(ExpectedConditions.attributeContains(By.xpath("//a[@idx='1']"),"class","iol_fst iol_fsst"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='iol_navl']")));
        driver.findElement(By.xpath("//a[@id='iol_navl']")).click();
        waitForSlideshow();

        wait.until(ExpectedConditions.attributeContains(By.xpath("//a[@idx='0']"),"class","iol_fst iol_fsst"));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@class = 'mainImage accessible nofocus']")));
        driver.findElement(By.xpath("//img[@class = 'mainImage accessible nofocus']")).click();

        String imgurl = driver.findElement(By.xpath("//img[@class = 'mainImage accessible nofocus']")).getAttribute("src");
        ArrayList handles = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window((String)handles.get(1));

        if (imgurl.equals(driver.getCurrentUrl())) System.out.println("The picture is in the new tab.");
        else System.out.println("The picture is not in new tab!");



    }

    private void waitForSlideshow() {
        int count = 1;
        while (count > 0) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Следующий результат поиска изображений']")));
                count = 0;
            } catch (Exception e) {
                count++;
            }
        }
    }

}
