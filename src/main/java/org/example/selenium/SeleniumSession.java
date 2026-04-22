package org.example.selenium;

import lombok.extern.slf4j.Slf4j;
import org.example.config.ParserConfig;
import org.example.config.SeleniumConfig;
import org.example.service.WebDriverService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class SeleniumSession {
    private final WebDriverService webDriverService;
    private final ParserConfig parserConfig;
    private WebDriver driver;
    private WebDriverWait wait;


    public SeleniumSession(WebDriverService webDriverService, ParserConfig parserConfig) {
        this.webDriverService = webDriverService;
        this.parserConfig = parserConfig;
    }

    public void start(){
        driver = webDriverService.createWebDriver();
        SeleniumConfig config = parserConfig.getSelenium();
        wait = new WebDriverWait(driver, Duration.ofSeconds(config.getPageLoadTimeout()));
    }

    public void stop(){
        if(driver == null){
            return;
        }
        try{
            driver.quit();
        }catch(Exception e){
            log.warn(e.getMessage());
        }
        finally {
            driver = null;
            wait = null;
        }
    }

    public WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("SeleniumSession не запущен");
        }
        return driver;
    }

    public WebDriverWait getWait() {
        if (wait == null) {
            throw new IllegalStateException("SeleniumSession не запущен");
        }
        return wait;
    }

}
