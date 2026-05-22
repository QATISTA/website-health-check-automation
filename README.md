# Website Health Check Automation

A Java Selenium automation tool that performs website health and security checks, captures screenshots, and generates a consolidated report automatically.

## Checks Performed

- HTTP/2 Validation
- DNS Propagation Check
- Google Safe Browsing Check
- Robots.txt Verification
- SSL Certificate Expiry Check
- Google PageSpeed Insights (Mobile & Desktop)
- HTML Validation

## Features

- Headless Chrome execution
- Automatic screenshot capture
- URL logging
- Google Drive integration
- Automated Google Docs report generation using Apps Script

## Tech Stack

- Java
- Selenium WebDriver
- ChromeDriver
- WebDriverManager
- Apache Commons IO
- Google Apps Script

## Run

1. Add target website URL in `config.txt`
2. Execute `AutomateHL.java`
3. Screenshots and URLs are generated automatically
4. Run the Apps Script to create the final Google Docs report

## Google Apps Script

The project includes a Google Apps Script that:

- Reads generated URLs
- Fetches screenshots from Google Drive
- Inserts screenshots into Google Docs
- Formats the final report automatically

Script file: `report-generator.gs`

## Google Apps Script

The report generation logic is available in:

google-apps-script/report-generator.gs

## Author

Tista Dutta
