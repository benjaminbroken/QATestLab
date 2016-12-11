/**
 * Created by benjaminbroken on 11.12.2016.
 * Generic script class can be used with any browser's web driver.
 */

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Iterator;
import java.util.List;

public class Script<T extends RemoteWebDriver> {

    private T driver;

    public Script(T driver) {
        this.driver = driver;
    }

    public void script() {

        driver.get("https://www.bing.com/");

        WebElement searchField = driver.findElement(By.xpath("//input[@class=\'b_searchbox\']"));

        searchField.clear();
        searchField.sendKeys(new CharSequence[]{"automation", Keys.ENTER});

        System.out.println("Pages' titles:");
        System.out.println('\t' + driver.getTitle());

        List searchResults = driver.findElements(By.xpath("//div[@class=\'b_title\']/h2/a"));
        System.out.println("Searched titles:");
        Iterator i = searchResults.iterator();
        while(i.hasNext()) {
            WebElement buffer = (WebElement)i.next();
            System.out.println('\t' + buffer.getText());
        }

    }

}
