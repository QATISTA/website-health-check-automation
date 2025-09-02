//success >> 2url,2 image pasted in doc
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecurityTest {

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
        //ss
        byte[] imageBytes1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        //Report link
        String url1 = "1. HTTP2 Status Report link: " + driver.getCurrentUrl();

        // ==== Robots.txt ====
        String domain = testUrl.replaceFirst("https?://", "").split("/")[0]
                .replaceAll("^(?:.*\\.)?([^.]+\\.com)$", "$1");
        driver.get("https://www." + domain + "/robots.txt");
        Thread.sleep(1000);

        byte[] imageBytes2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        String url2 = driver.getCurrentUrl();

        driver.quit();

        // ==== Send both URLs and both images in a SINGLE POST ====
        sendBothUrlsAndImages(webhookURL,
        		url1, imageBytes1, "http2.png",
        		url2, imageBytes2, "robots.png");
    }

    private static void sendBothUrlsAndImages(String webhookURL,
    		String url1, byte[] imageBytes1, String fileName1,
            String url2, byte[] imageBytes2, String fileName2) throws Exception {
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

        // ---- End boundary ----
        out.write(("--" + boundary + "--\r\n").getBytes());
        out.flush();

        System.out.println("Webhook response code: " + conn.getResponseCode());
    }
}
