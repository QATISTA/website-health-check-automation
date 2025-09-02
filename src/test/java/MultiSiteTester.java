import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException; // NEW
import org.apache.commons.io.FileUtils; // NEW
import org.openqa.selenium.OutputType; // NEW
import org.openqa.selenium.TakesScreenshot; // NEW
import java.awt.image.BufferedImage; // NEW
import javax.imageio.ImageIO;         // NEW

public class MultiSiteTester {
    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        try {
            driver.manage().window().maximize();
            String testUrl = "https://staging.physiciansgrade.com/product/cbd-roll-on-gel/";
           //---------- HTTP2 Status -----------------
            driver.get("https://tools.keycdn.com/http2-test");
            driver.findElement(By.id("url")).sendKeys(testUrl);
            driver.findElement(By.id("http2Btn")).click();
            //Report link
            System.out.println("Report Link: " + driver.getCurrentUrl());
            //Scroll down
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
            Thread.sleep(1000); // small pause for scroll to settle
            
            File ss1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(ss1, new File("G:\\My Drive\\Automate_SS/http2.png")); // NEW
            
          //-------------------------------HTTPS Redirection---------------------------------------------------------
            driver.get("https://wheregoes.com/");
            String testUrl_http = testUrl.replaceFirst("^https", "http");
            System.out.println(testUrl_http);
           
            driver.findElement(By.id("url")).sendKeys(testUrl_http);
            driver.findElement(By.id("form_button")).click();
            //Thread.sleep(3000);
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p[class='date'] i"))
                );
          //Report link
            System.out.println("HTTPS Redirection check Report Link: " + driver.getCurrentUrl());
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='33%'");
            // Wait a bit for layout to adjust
            Thread.sleep(1000);  
            File ss6 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(ss6, new File("screenshots/https_redirection.png")); // NEW
            
         
            
            
           //-----------------------------------------------------------------------------------------------------------------
          // ----------------Domain Test URL WhatsMyDNS ----------------------------------------------------------------------
             driver.get("https://www.whatsmydns.net/");
             driver.findElement(By.id("q")).sendKeys(testUrl);
             driver.findElement(By.xpath("(//span[@class='ml-2'])[1]")).click();
             new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                 ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='ml-2'])[1]"))
             );
             //Report link
             System.out.println("WhatsMyDNS Report Link: " + driver.getCurrentUrl());   
             Thread.sleep(6500);
               
          // Zoom out to 50%
             ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='50%'");

             // Wait a bit for layout to adjust
             Thread.sleep(1000);

             // Take screenshot of the viewport
             File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
             FileUtils.copyFile(screenshot, new File("screenshots/screenshot_zoomed.png"));

             // Restore zoom to normal if needed
             ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='100%'");
             Thread.sleep(3000);  
             //-----------------------------------------------------------------------------------------------------------------
             //-------------------------------Safe Browsing Site Status---------------------------------------------------------
             driver.get("https://transparencyreport.google.com/safe-browsing/search?hl=en");
             driver.findElement(By.xpath("//input[@placeholder='Search by URL']")).sendKeys(testUrl);
             
             driver.findElement(By.xpath("//i[normalize-space()='search']")).click();
             Thread.sleep(5000);
             //Report link
             System.out.println("Report Link: " + driver.getCurrentUrl());
          // Zoom out to 75%
             ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='85%'");
             // Wait a bit for layout to adjust
             Thread.sleep(1000);             
             File ss3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
             FileUtils.copyFile(ss3, new File("screenshots/safe_browsing.png")); // NEW
             Thread.sleep(3000);
           //-----------------------------------------------------------------------------------------------------------------
             //-------------------------------robots.txt---------------------------------------------------------
          // 1. Remove protocol (https:// or http://)
             String withoutProtocol = testUrl.replaceFirst("https?://", "");
          // Split by "/" from the protocol-free string
          String host = withoutProtocol.split("/")[0]; // staging.physiciansgrade.com
          // Extract domain part (e.g., physiciansgrade.com)
          String domain = host.replaceAll("^(?:.*\\.)?([^.]+\\.com)$", "$1");
          // Build the robots.txt URL
          String testUrl1 = "https://www." + domain + "/robots.txt";
          System.out.println("Robots Report link:" + testUrl1);
          Thread.sleep(3000);
          driver.get(testUrl1);     
             Thread.sleep(1000); // small pause  to settle
             
             File ss4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
             FileUtils.copyFile(ss4, new File("screenshots/robots.png")); // NEW
             
             //-----------------------------------------------------------------------------------------------------------------
             //-------------------------------Domain Expiry check---------------------------------------------------------
             String testUrl2 = "https://www." + domain;
             driver.get("https://www.digicert.com/help/");
             driver.findElement(By.id("host")).sendKeys(testUrl2);
             driver.findElement(By.id("check-server-button")).click();
             Thread.sleep(3000);
             //Report link
             System.out.println("Domain Expiry check Report Link: " + driver.getCurrentUrl());
             //Scroll down
             ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 750);");
             Thread.sleep(1000); // small pause for scroll to settle
             
             File ss5 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
             FileUtils.copyFile(ss5, new File("screenshots/domain_expiry.png")); // NEW
             //-----------------------------------------------------------------------------------------------------------------
             
             
             
             
             //-----------------------------------------------------------------------------------------------------------------
             //-------------------------------SSL Grade Test---------------------------------------------------------
             driver.get("https://www.ssllabs.com/ssltest");
             driver.findElement(By.xpath("//input[@name='d']")).sendKeys(testUrl);
             driver.findElement(By.xpath("//input[@value='Submit']")).click();
             //*** Wait until the input box is no longer visible
             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
             wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//input[@name='d']")));
             ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='50%'");
          // Wait a bit for layout to adjust
             Thread.sleep(1000);  
             File ss7 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
             FileUtils.copyFile(ss7, new File("screenshots/sslLab.png")); // NEW
           //-----------------------------------------------------------------------------------------------------------------
             //-------------------------------Web page Test---------------------------------------------------------
             driver.get("https://pagespeed.web.dev/?utm_source=psi&utm_medium=redirect");
             driver.findElement(By.xpath("//input[@id='i2']")).sendKeys(testUrl);
             driver.findElement(By.xpath("//span[normalize-space()='Analyze']")).click();
             //*** Wait till desktop tab appears
             new WebDriverWait(driver, Duration.ofSeconds(180)).until(
                     ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@id='performance'])[1]"))
                 );
           //mobile
              driver.findElement(By.xpath("//button[@id='mobile_tab']")).click();
              ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='70%'");
           // Wait a bit for layout to adjust
              Thread.sleep(1000);  
              File ss9 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
              FileUtils.copyFile(ss9, new File("screenshots/wpt_mobile.png")); // NEW
             driver.findElement(By.xpath("//button[@id='desktop_tab']")).click();
             new WebDriverWait(driver, Duration.ofSeconds(180)).until(
                     ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@id='screenshot-thumbnails'])[2]"))
                 );
         
             ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='70%'");
          // Wait a bit for layout to adjust
             Thread.sleep(1000);  
             File ss8 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
             FileUtils.copyFile(ss8, new File("screenshots/wpt_desktop.png")); // NEW
             //mobile
            // driver.findElement(By.xpath("//button[@id='mobile_tab']")).click();
            // ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='70%'");
             
        } finally {
            driver.quit();
        }
    }
}
