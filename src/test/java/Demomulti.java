// to paste all the report link in the doc



// ==== Imports remain the same ====
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

// ==== Added for Make Webhook Integration ====
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.OutputStream;

public class Demomulti {

    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

       
            driver.manage().window().maximize();
            String testUrl = "https://staging.physiciansgrade.com/product/cbd-roll-on-gel/";

            // =================== Your Existing Steps Start ===================

            // HTTP2 Status
            driver.get("https://tools.keycdn.com/http2-test");
            driver.findElement(By.id("url")).sendKeys(testUrl);
            driver.findElement(By.id("http2Btn")).click();
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
            Thread.sleep(1000);
            File ss1 = saveScreenshot(driver, "screenshots/http2.png");
          //  sendSingleToMake(driver.getCurrentUrl(), ss1); // <-- UPDATED
            String url1 = "1. HTTP2 Status Report link: "+ driver.getCurrentUrl();
            Path image1 = Path.of("screenshots/http2.png");
            Files.copy(ss1.toPath(), image1, StandardCopyOption.REPLACE_EXISTING);

           

            // Robots.txt
            String withoutProtocol = testUrl.replaceFirst("https?://", "");
            String host = withoutProtocol.split("/")[0];
            String domain = host.replaceAll("^(?:.*\\.)?([^.]+\\.com)$", "$1");
            String testUrl1 = "https://www." + domain + "/robots.txt";
            driver.get(testUrl1);
            Thread.sleep(1000);
            File ss5 = saveScreenshot(driver, "screenshots/robots.png");
          //  sendSingleToMake(driver.getCurrentUrl(), ss5); // <-- UPDATED
            String url2 = driver.getCurrentUrl();
            Path image2 = Path.of("screenshots/robots.png");
            Files.copy(ss5.toPath(), image2, StandardCopyOption.REPLACE_EXISTING);

            driver.quit();
            
            // =================== Your Existing Steps End ===================
            String webhookURL = "https://hook.us2.make.com/y3sdfc580f5af29n6swstcknwjqic6y1";

            // Encode file paths and URLs
            String data = "url1=" + URLEncoder.encode(url1, "UTF-8") +
                          "&url2=" + URLEncoder.encode(url2, "UTF-8") +
                          "&image1=" + URLEncoder.encode(image1.toAbsolutePath().toString(), "UTF-8") +
                          "&image2=" + URLEncoder.encode(image2.toAbsolutePath().toString(), "UTF-8");

            // Send HTTP GET request to webhook (simplest approach)
            URL url = new URL(webhookURL + "?" + data);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            System.out.println("Webhook response code: " + responseCode);
        
    }

    // ==== Helper Method to Save Screenshot ====
    private static File saveScreenshot(WebDriver driver, String filePath) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(filePath);
        FileUtils.copyFile(src, dest);
        return dest;
    }
}

    
       