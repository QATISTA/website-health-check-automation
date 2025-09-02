//paste 1 report link+1 image in the doc


import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.io.FileUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class SecurityAutomate {

    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        String testUrl = "https://staging.physiciansgrade.com/product/cbd-roll-on-gel/";
        String webhookURL = "https://hook.us2.make.com/y3sdfc580f5af29n6swstcknwjqic6y1";

        // ==== HTTP2 Status ====
        driver.get("https://tools.keycdn.com/http2-test");
        driver.findElement(By.id("url")).sendKeys(testUrl);
        driver.findElement(By.id("http2Btn")).click();
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
        Thread.sleep(1000);

        File ss1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path image1 = Path.of("screenshots/http2.png");
        Files.copy(ss1.toPath(), image1, StandardCopyOption.REPLACE_EXISTING);
        String url1 = "1. HTTP2 Status Report link: " + driver.getCurrentUrl();

        sendUrlAndImage(webhookURL, url1, image1, "http2.png");

        // ==== Robots.txt ====
        String domain = testUrl.replaceFirst("https?://", "").split("/")[0].replaceAll("^(?:.*\\.)?([^.]+\\.com)$", "$1");
        driver.get("https://www." + domain + "/robots.txt");
        Thread.sleep(1000);

        File ss2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path image2 = Path.of("screenshots/robots.png");
        Files.copy(ss2.toPath(), image2, StandardCopyOption.REPLACE_EXISTING);
        String url2 = driver.getCurrentUrl();

        sendUrlAndImage(webhookURL, url2, image2, "robots.png");

        driver.quit();
    }

    private static void sendUrlAndImage(String webhookURL, String urlValue, Path imagePath, String fileName) throws Exception {
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        HttpURLConnection conn = (HttpURLConnection) new URL(webhookURL).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream out = conn.getOutputStream();

        // Send URL
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"url\"\r\n\r\n").getBytes());
        out.write((urlValue + "\r\n").getBytes());

        // Send file
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n").getBytes());
        out.write("Content-Type: image/png\r\n\r\n".getBytes());
        Files.copy(imagePath, out);

        // End boundary
        out.write(("\r\n--" + boundary + "--\r\n").getBytes());
        out.flush();

        System.out.println("Webhook response code: " + conn.getResponseCode());
    }
}
