//success >> if we remove the last data i.e imageBytes8 related things
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class SecurityAndPerformance {

    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        String testUrl = "https://staging.physiciansgrade.com/product/cbd-roll-on-gel/";
        String webhookURL = "https://hook.us2.make.com/vgjj82d1huxwx8giwtutjvuqaheg5wsu";

        
        // ==== HTTP2 Status ====
        driver.get("https://tools.keycdn.com/http2-test");
        driver.findElement(By.id("url")).sendKeys(testUrl);
        driver.findElement(By.id("http2Btn")).click();
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
        Thread.sleep(1000);
        //ss
        byte[] imageBytes1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        //Report link
        String url1 = "1. HTTP2 Status Report link: " + driver.getCurrentUrl();

        // ==== Robots.txt ========================================================================
        String domain = testUrl.replaceFirst("https?://", "").split("/")[0]
                .replaceAll("^(?:.*\\.)?([^.]+\\.com)$", "$1");
        driver.get("https://www." + domain + "/robots.txt");
        Thread.sleep(1000);

        byte[] imageBytes2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        String url2 ="2. Robots.txt testing: "+ driver.getCurrentUrl();
        
        
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
        System.out.println("3. Report Link for HTTPS Redirection check : " + driver.getCurrentUrl());
        String url6 = "3. HTTPS Redirection Report link: " + driver.getCurrentUrl();
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='33%'");
        // Wait a bit for layout to adjust
        Thread.sleep(1000);  
        //ss
        byte[] imageBytes6 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      //  FileUtils.copyFile(ss6, new File("screenshots/https_redirection.png")); // NEW
        //-----------------------------------------------------------------------------------------------------------------
        // ----------------Domain Test URL WhatsMyDNS ----------------------------------------------------------------------
           driver.get("https://www.whatsmydns.net/");
           driver.findElement(By.id("q")).sendKeys(testUrl);
           driver.findElement(By.xpath("(//span[@class='ml-2'])[1]")).click();
           new WebDriverWait(driver, Duration.ofSeconds(20)).until(
               ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='ml-2'])[1]"))
           );
           //Report link****                     
           String url4 = "4. Domain Test URL Report Link : " + driver.getCurrentUrl();
           Thread.sleep(6500);
             
        // Zoom out to 50%
           ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='50%'");

           // Wait a bit for layout to adjust
           Thread.sleep(1000);

           // ss*****     
           byte[] imageBytes4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
           
           // Restore zoom to normal if needed
           ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='100%'");
           Thread.sleep(3000);  
           
         //-----------------------------------------------------------------------------------------------------------------
           //-------------------------------Safe Browsing Site Status---------------------------------------------------------
           driver.get("https://transparencyreport.google.com/safe-browsing/search?hl=en");
           driver.findElement(By.xpath("//input[@placeholder='Search by URL']")).sendKeys(testUrl);
           
           driver.findElement(By.xpath("//i[normalize-space()='search']")).click();
           Thread.sleep(5000);
           //Report link****  
           String url5 = "5. Safe browsing site status Report Link : " + driver.getCurrentUrl();
        // Zoom out to 75%
           ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='85%'");
           // Wait a bit for layout to adjust
           Thread.sleep(1000);  
           //ss******          
           byte[] imageBytes5 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
          
           Thread.sleep(3000);
         //-----------------------------------------------------------------------------------------------------------------
           //-------------------------------Domain Expiry check---------------------------------------------------------
           String testUrl2 = "https://www." + domain;
           driver.get("https://www.digicert.com/help/");
           driver.findElement(By.id("host")).sendKeys(testUrl2);
           driver.findElement(By.id("check-server-button")).click();
           Thread.sleep(3000);
          
         //Report link****  
           String url7 = "6. Domain Expiry check Report Link: : " + driver.getCurrentUrl();
           //Scroll down
           ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 750);");
           Thread.sleep(1000); // small pause for scroll to settle
                      
         //ss******          
           byte[] imageBytes7 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
         //-----------------------------------------------------------------------------------------------------------------
           //-------------------------------SSL Grade Test---------------------------------------------------------
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
           //Report link****  
           String url8 = "7. SSL Grade Report Link: : " + driver.getCurrentUrl();
          //ss*****
           byte[] imageBytes8 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
           
          
        driver.quit();

        // ==== Send both URLs and both images in a SINGLE POST ====
        sendBothUrlsAndImages(webhookURL,
        		url1, imageBytes1, "http2.png",
        		url2, imageBytes2, "robots.png",
        		url6, imageBytes6, "https_redirect.png",
        		url4, imageBytes4, "whatmydns.png",
        		url5, imageBytes5, "safebrowsing.png",
        		url7, imageBytes7, "domainexpiry.png",
        		url8, imageBytes8, "sslgrade.png"
        		
        		);
    }

    private static void sendBothUrlsAndImages(String webhookURL,
    		String url1, byte[] imageBytes1, String fileName1,
            String url2, byte[] imageBytes2, String fileName2,
            String url6, byte[] imageBytes6, String fileName6,
            String url4, byte[] imageBytes4, String fileName4,
            String url5, byte[] imageBytes5, String fileName5,
            String url7, byte[] imageBytes7, String fileName7,
            String url8, byte[] imageBytes8, String fileName8
            
    		) throws Exception {
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        HttpURLConnection conn = (HttpURLConnection) new URL(webhookURL).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream out = conn.getOutputStream();

        // ---- Send URL1 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write("Content-Disposition: form-data; name=\"url1\"\r\n\r\n".getBytes());
        out.write((url1 + "\r\n").getBytes());

        // ---- Send IMAGE1 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file1\"; filename=\"" + fileName1 + "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        out.write(imageBytes1);
        out.write("\r\n".getBytes());

        // ---- Send URL2 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write("Content-Disposition: form-data; name=\"url2\"\r\n\r\n".getBytes());
        out.write((url2 + "\r\n").getBytes());

        // ---- Send IMAGE2 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file2\"; filename=\"" + fileName2 + "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        out.write(imageBytes2);
        out.write("\r\n".getBytes());

        // ---- Send URL6 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write("Content-Disposition: form-data; name=\"url6\"\r\n\r\n".getBytes());
        out.write((url6+ "\r\n").getBytes());
     // ---- Send IMAGE6----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file6\"; filename=\"" + fileName6+ "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        out.write(imageBytes6);
        out.write("\r\n".getBytes());
        
        
     // ---- Send URL4 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write("Content-Disposition: form-data; name=\"url4\"\r\n\r\n".getBytes());
        out.write((url4+ "\r\n").getBytes());
        // ---- Send IMAGE4----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file4\"; filename=\"" + fileName4+ "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        out.write(imageBytes4);
        out.write("\r\n".getBytes());
        
        // ---- Send URL5 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write("Content-Disposition: form-data; name=\"url5\"\r\n\r\n".getBytes());
        out.write((url5+ "\r\n").getBytes());
        // ---- Send IMAGE5----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file5\"; filename=\"" + fileName5+ "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        out.write(imageBytes5);
        out.write("\r\n".getBytes());
        
        // ---- Send URL7 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write("Content-Disposition: form-data; name=\"url7\"\r\n\r\n".getBytes());
        out.write((url7+ "\r\n").getBytes());
        // ---- Send IMAGE7----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file7\"; filename=\"" + fileName7+ "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        out.write(imageBytes7);
        out.write("\r\n".getBytes());
        
        
     // ---- Send URL8 ----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write("Content-Disposition: form-data; name=\"url8\"\r\n\r\n".getBytes());
        out.write((url8+ "\r\n").getBytes());
        // ---- Send IMAGE8----
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file8\"; filename=\"" + fileName8+ "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        out.write(imageBytes8);
        out.write("\r\n".getBytes());
        
        // ---- End boundary ----
        out.write(("--" + boundary + "--\r\n").getBytes());
        out.flush();

        System.out.println("Webhook response code: " + conn.getResponseCode());
    }
}
