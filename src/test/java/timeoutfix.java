// headless new

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

public class timeoutfix {
    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();

        // ------------------- CLEANUP OLD DATA -------------------
        String screenshotFolder = "G:\\My Drive\\Automate_SS";
        String urlLogPath = "G:/My Drive/Automation/urls.txt";

        File folder = new File(screenshotFolder);
        if (folder.exists()) {
            FileUtils.cleanDirectory(folder);
        } else {
            folder.mkdirs();
        }

        Path path = Paths.get(urlLogPath);
        if (Files.exists(path)) {
            Files.write(path, new byte[0]);
        } else {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        // ---------------------------------------------------------

        // ---------- Headless Chrome Setup ----------
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // 👇 Fake normal user-agent
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/139.0.0.0 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);
        try {
        	InputStream is = timeoutfix.class.getClassLoader().getResourceAsStream("config.txt");
        	if (is == null) {
        	    throw new RuntimeException("config.txt not found!");
        	}
        	String testUrl = new String(is.readAllBytes()).trim();

           // String testUrl = "https://www.theshopzoneonline.com/?affid=1";

            // ================== HTTP2 Status ==================
            driver.get("https://tools.keycdn.com/http2-test");
            driver.findElement(By.id("url")).sendKeys(testUrl);
            driver.findElement(By.id("http2Btn")).click();
            Thread.sleep(2000);
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
            Thread.sleep(2000);
            saveResult(driver, screenshotFolder, path, "01_http2.png", "1. HTTP2 Report Link");

            // ================== HTTPS Redirection ==================
            driver.navigate().to("https://wheregoes.com/");
            String testUrl_http = testUrl.replaceFirst("^https", "http");
            WebDriverWait wait_1 = new WebDriverWait(driver, Duration.ofSeconds(60));
            WebElement urlInput1 = wait_1.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("url"))
            );
            urlInput1.sendKeys(testUrl_http);

            WebDriverWait wait_2 = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement formButton = wait_2.until(
                ExpectedConditions.elementToBeClickable(By.id("form_button"))
            );
            formButton.click();

            new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.date i"))
            );
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='55%'");
            Thread.sleep(1000);
            saveResult(driver, screenshotFolder, path, "02_https_redirection.png", "2. HTTPS Redirection Report Link");

            // ================== WhatsMyDNS ==================
            driver.get("https://www.whatsmydns.net/");

            // Normalize URL for testing
            String domainToTest = testUrl.replaceFirst("^https?://", "").split("/")[0]; 
            String urlWithWww = "www." + domainToTest.replaceFirst("^www\\.", ""); // force www
            String urlWithoutWww = domainToTest.replaceFirst("^www\\.", "");       // remove www

            // If URL already has "www", test both www and non-www
            if (domainToTest.startsWith("www.")) {
                // ----- Case 1: With www -----
                WebDriverWait wait_3a = new WebDriverWait(driver, Duration.ofSeconds(30));
                WebElement qInput1 = wait_3a.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("q"))
                );
                qInput1.sendKeys(urlWithWww);

                WebDriverWait wait_4a = new WebDriverWait(driver, Duration.ofSeconds(30));
                WebElement dnsButton1 = wait_4a.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class='ml-2'])[1]"))
                );
                dnsButton1.click();

                new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='ml-2'])[1]"))
                );
                Thread.sleep(6500);
                ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='50%'");
                Thread.sleep(1000);
                saveResult(driver, screenshotFolder, path, "03a_whatsmydns_www.png", "3a. Domain Test URL (with www)");
                Thread.sleep(1000);

                // Refresh page for next test
                driver.get("https://www.whatsmydns.net/");

                // ----- Case 2: Without www -----
                WebDriverWait wait_3b = new WebDriverWait(driver, Duration.ofSeconds(30));
                WebElement qInput2 = wait_3b.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("q"))
                );
                qInput2.sendKeys(urlWithoutWww);

                WebDriverWait wait_4b = new WebDriverWait(driver, Duration.ofSeconds(30));
                WebElement dnsButton2 = wait_4b.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class='ml-2'])[1]"))
                );
                dnsButton2.click();

                new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='ml-2'])[1]"))
                );
                Thread.sleep(6500);
                ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='50%'");
                Thread.sleep(1000);
                saveResult(driver, screenshotFolder, path, "03b_whatsmydns_nonwww.png", "3b. Domain Test URL (without www)");
                Thread.sleep(1000);

            } else {
                // ----- Case 3: No www in input URL → Only test once -----
                WebDriverWait wait_3 = new WebDriverWait(driver, Duration.ofSeconds(30));
                WebElement qInput = wait_3.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("q"))
                );
                qInput.sendKeys(domainToTest);

                WebDriverWait wait_4 = new WebDriverWait(driver, Duration.ofSeconds(30));
                WebElement dnsButton = wait_4.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class='ml-2'])[1]"))
                );
                dnsButton.click();

                new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='ml-2'])[1]"))
                );
                Thread.sleep(6500);
                ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='50%'");
                Thread.sleep(1000);
                saveResult(driver, screenshotFolder, path, "03_whatsmydns.png", "3. Domain Test URL Report Link");
                Thread.sleep(1000);
            }

            // ================== Safe Browsing ==================
            driver.get("https://transparencyreport.google.com/safe-browsing/search?hl=en");
            Thread.sleep(3500);
            driver.findElement(By.xpath("//input[@placeholder='Search by URL']")).sendKeys(testUrl);
            driver.findElement(By.xpath("//i[normalize-space()='search']")).click();
            Thread.sleep(5000);
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='85%'");
            Thread.sleep(1000);
            saveResult(driver, screenshotFolder, path, "04_safe_browsing.png", "4. Safe Browsing Site Status Report Link");

            // ================== Robots.txt ==================
            String withoutProtocol = testUrl.replaceFirst("https?://", "");
            String host = withoutProtocol.split("/")[0];
            String domain = host.replaceAll("^(?:.*\\.)?([^.]+\\.com)$", "$1");

            String testUrl1 = "https://" + domain + "/robots.txt";

            try {
                driver.get(testUrl1);
            } catch (WebDriverException e) {
                System.out.println("Direct domain failed, retrying with www...");
                testUrl1 = "https://www." + domain + "/robots.txt";
                driver.get(testUrl1);
            }

            Thread.sleep(2000);
            saveResult(driver, screenshotFolder, path, "05_robots.png", "5. Robots.txt Report Link");

            // ================== Domain Expiry ==================
            String testUrl2 = domain;
            driver.get("https://www.digicert.com/help/");
            driver.findElement(By.id("host")).sendKeys(testUrl2);
            driver.findElement(By.id("check-server-button")).click();
            Thread.sleep(12000);

            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(100));
            WebElement element = wait1.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='TLS Certificate has not been revoked']"))
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
// result text capture
         // Wait for the paragraph element to appear
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement expiryElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(),'The certificate expires')]")
                )
            );

            // Get the text
            String expiryText = expiryElement.getText();
            System.out.println("Certificate Expiry: " + expiryText);

            Thread.sleep(1000);
            saveResult(driver, screenshotFolder, path,
                    "06_domain_expiry.png",
                    "6. Domain Expiry check Report Link",
                    expiryText);
            // ================== SSL Labs ==================
            driver.get("https://www.ssllabs.com/ssltest");
            driver.findElement(By.xpath("//input[@name='d']")).sendKeys(testUrl);
            driver.findElement(By.xpath("//input[@value='Submit']")).click();
            new WebDriverWait(driver, Duration.ofSeconds(10000)) 
              .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='index.html']")));
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='80%'");
            Thread.sleep(1000);
            saveResult(driver, screenshotFolder, path, "07_ssllabs.png", "7. SSL Grade Report Link");

            // ================== PageSpeed Insights ==================
            driver.get("https://pagespeed.web.dev/?utm_source=psi&utm_medium=redirect");
            WebDriverWait wait_5 = new WebDriverWait(driver, Duration.ofSeconds(60));
            WebElement psiInput = wait_5.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='i2']"))
            );
            psiInput.sendKeys(testUrl);
            WebDriverWait wait_6 = new WebDriverWait(driver, Duration.ofSeconds(60));
            WebElement analyzeBtn = wait_6.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Analyze']"))
            );
            analyzeBtn.click();

            // Mobile tab
            new WebDriverWait(driver, Duration.ofSeconds(180)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@id='performance'])[1]"))
            );
            WebDriverWait wait_7 = new WebDriverWait(driver, Duration.ofSeconds(60));
            WebElement mobileTab = wait_7.until(
                ExpectedConditions.elementToBeClickable(By.id("mobile_tab"))
            );
            mobileTab.click();
            Thread.sleep(1000);
            WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(100));
            WebElement element3 = wait3.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(normalize-space(text()),'Diagnose performance')]")
                )
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element3);

            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='75%'");
            saveResult(driver, screenshotFolder, path, "08_pagespeed_mobile.png", "8. Google PageSpeed Mobile Report Link");

            // Desktop tab
            WebDriverWait wait_8 = new WebDriverWait(driver, Duration.ofSeconds(60));
            WebElement desktopTab = wait_8.until(
                ExpectedConditions.elementToBeClickable(By.id("desktop_tab"))
            );
            desktopTab.click();
            new WebDriverWait(driver, Duration.ofSeconds(180)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@id='screenshot-thumbnails'])[2]"))
            );

            Thread.sleep(1000);
            WebDriverWait wait4 = new WebDriverWait(driver, Duration.ofSeconds(100));
            WebElement element4 = wait4.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(normalize-space(text()),'Diagnose performance')]")
                )
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element4);

            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='75%'");

            saveResult(driver, screenshotFolder, path, "09_pagespeed_desktop.png", "9. Google PageSpeed Desktop Report Link");
            
            
		// HTML Optimisation Report
            driver.get("https://validator.w3.org/nu/");
            driver.findElement(By.xpath("//input[@id='doc']")).sendKeys(testUrl);
        
            driver.findElement(By.xpath("//input[@id='submit']")).click();
         // wait for result generate
			/*
			 * WebDriverWait wait_msg = new WebDriverWait(driver, Duration.ofSeconds(60));
			 * WebElement messageFilter = wait_msg.until(
			 * ExpectedConditions.presenceOfElementLocated(By.xpath(
			 * "//div[@class='message_filtering']")) );
			 */
            Thread.sleep(24);
            saveResult(driver, screenshotFolder, path, "10_HTML_Optimization.png", "10. HTML Optimisation Report Link");


        } finally {
            driver.quit();
        }
    }

    // ------------------- Helper Method -------------------
 // Overloaded helper method
    private static void saveResult(WebDriver driver, String folder, Path path,
            String fileName, String label, String extraInfo) throws IOException {
        File ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(ss, new File(folder + "\\" + fileName));

        String currentUrl = driver.getCurrentUrl();
        String line = label + ": " + currentUrl;
        if (extraInfo != null && !extraInfo.isEmpty()) {
            line += " | " + extraInfo;
        }
        line += "\n";

        Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
        System.out.println(line);
    }

    // Existing method stays the same for other calls
    private static void saveResult(WebDriver driver, String folder, Path path,
            String fileName, String label) throws IOException {
        saveResult(driver, folder, path, fileName, label, null);
    }

}
