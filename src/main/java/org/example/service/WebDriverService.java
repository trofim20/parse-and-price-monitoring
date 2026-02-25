package org.example.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;

import org.example.config.ParserConfig;
import org.example.config.SeleniumConfig;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class WebDriverService {
    private final ParserConfig parserConfig;
    private static final Logger log = LoggerFactory.getLogger(WebDriverService.class);

    public WebDriverService(ParserConfig parserConfig) {
        this.parserConfig = parserConfig;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing Selenium Driver");
    }

    public WebDriver createWebDriver(){
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = createChromeOptions();
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        configureDriver(driver);
        return driver;
    }

    private ChromeOptions createChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        SeleniumConfig seleniumConfig = parserConfig.getSelenium();

        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.addArguments("--user-agent=" + seleniumConfig.getUserAgent());
        chromeOptions.addArguments("--window-size=" + seleniumConfig.getWindowSize());
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-images");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-running-insecure-content");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-default-apps");
        chromeOptions.addArguments("--disable-infobars");
        if (seleniumConfig.isHeadless()) {
            chromeOptions.addArguments("--headless=new");
        }

        chromeOptions.addArguments("--incognito");


        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.setExperimentalOption("useAutomationExtension", false);

        return chromeOptions;
    }

    private void configureDriver(WebDriver driver) {
        SeleniumConfig seleniumConfig = parserConfig.getSelenium();

        int pageLoadTimeout = seleniumConfig.getPageLoadTimeout();
        int implicitWaitTimeout = seleniumConfig.getImplicitWaitTimeout();
        int scriptTimeout = 30;

        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout))
                .implicitlyWait(Duration.ofSeconds(implicitWaitTimeout))
                .scriptTimeout(Duration.ofSeconds(scriptTimeout));
    }
}
