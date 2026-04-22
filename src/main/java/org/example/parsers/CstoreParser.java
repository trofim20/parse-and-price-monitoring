package org.example.parsers;

import lombok.extern.slf4j.Slf4j;
import org.example.base.SeleniumBaseParser;
import org.example.config.ParserConfig;
import org.example.extractor.SeleniumProductCardExtractorImpl;
import org.example.selenium.SeleniumSession;
import org.example.service.DetailPageService;
import org.example.util.PageUrlBuilderImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class CstoreParser extends SeleniumBaseParser {
    private final SeleniumSession seleniumSession;

    public CstoreParser(ParserConfig parserConfig, PageUrlBuilderImpl pageUrlBuilder, SeleniumSession seleniumSession, SeleniumProductCardExtractorImpl seleniumProductCardExtractor, DetailPageService detailPageService, SeleniumSession seleniumSession1) {
        super("cstore", parserConfig, pageUrlBuilder, seleniumSession, seleniumProductCardExtractor, detailPageService);
        this.seleniumSession = seleniumSession1;
    }

    @Override
    protected void login() {
        String loginUrl = "https://www.cstore.ru/auth/login";
        log.info("Авторизация на cstore: {}", loginUrl);

        navigateTo(loginUrl);

        sleep(2000);
        try {
            JavascriptExecutor js = (JavascriptExecutor) seleniumSession.getDriver();
            try {
                WebElement cookieBtn = seleniumSession.getDriver().findElement(
                        By.cssSelector("#cookie-consent .cookie-consent__button"));
                js.executeScript("arguments[0].click();", cookieBtn);
                sleep(1000);
            } catch (NoSuchElementException ignored) {}

            WebElement loginField = seleniumSession.getDriver().findElement(
                    By.cssSelector("input[name='login']"));
            WebElement passField = seleniumSession.getDriver().findElement(
                    By.cssSelector("input[name='password']"));
            WebElement submitBtn = seleniumSession.getDriver().findElement(
                    By.cssSelector("button[type='submit'].registration-submit"));
            js.executeScript("arguments[0].value = '';", loginField);
            loginField.sendKeys("danis10");

            js.executeScript("arguments[0].value = '';", passField);
            passField.sendKeys("9630367380");


            js.executeScript("arguments[0].scrollIntoView(true);", submitBtn);
            sleep(500);
            js.executeScript("arguments[0].click();", submitBtn);

            sleep(4000);

        } catch (NoSuchElementException e) {
            log.error("Элемент не найден: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Ошибка авторизации: {}", e.getMessage());
        }
    }
}
