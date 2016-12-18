/**
 * Created by benjaminbroken on 11.12.2016.
 * Entry point of the program. Take a look at Script class for the main functionality.
 */

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

    public static void main(String[] args) {

        //driver init
        String property = System.getProperty("user.dir") + "/driver/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", property);
        ChromeDriver chromeDriver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(chromeDriver,10, 100);


        //running script with specified driver
        Script<ChromeDriver> testScript  = new Script<ChromeDriver>(chromeDriver, wait);
        testScript.script();

        //quitting
        chromeDriver.quit();


    }
}
