package org.example.parsers;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.example.base.SeleniumBaseParser;
import org.example.config.ParserConfig;
import org.example.entity.Product;
import org.example.extractor.SeleniumProductCardExtractorImpl;
import org.example.selenium.SeleniumSession;
import org.example.service.DetailPageService;
import org.example.util.PageUrlBuilderImpl;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class IxoraAutoParser extends SeleniumBaseParser {
    private boolean firstPageHandled = false;
    public IxoraAutoParser(ParserConfig parserConfig, PageUrlBuilderImpl pageUrlBuilder, SeleniumProductCardExtractorImpl seleniumProductCardExtractor, DetailPageService detailPageService, SeleniumSession seleniumSession) {
        super("ixora", parserConfig, pageUrlBuilder, seleniumSession, seleniumProductCardExtractor, detailPageService);
    }


    @Override
    protected void afterNavigateTo(String url) {
        if (!firstPageHandled) {
            clickAllProductButtonMultipleTimes();
            firstPageHandled = true;
        }
    }

    @Override
    protected Product parseProductCard(WebElement productCard) {
        Product product = super.parseProductCard(productCard);
        try{
            String selector = siteConfig.getSelector("price");
            WebElement priceElement = productCard.findElement(By.cssSelector(selector));

            String priceText = (String) ((JavascriptExecutor) getSeleniumSession().getDriver())
                    .executeScript(
                            "const el = arguments[0]; " +
                                    "let text = ''; " +
                                    "for (let node of el.childNodes) { " +
                                    "  if (node.nodeType === Node.TEXT_NODE) text += node.textContent; " +
                                    "} " +
                                    "return text.trim().replace(/\\s+/g, ' ');",
                            priceElement
                    );

            String res = priceText.isEmpty() ? null : priceText;
            product.setPrice(getSeleniumProductCardExtractor().extractPriceAsBigDecimal(res));
        }catch (NoSuchElementException e) {
            log.warn("Цена не найдена в карточке");
            return null;
        }
        return product;
    }

    private void clickAllProductButtonMultipleTimes() {
        By showAllProductBtn = By.cssSelector(
                "div > div.layout__content > div.layout__content-body > div > div > " +
                        "div:nth-child(4) > div.row > div.ml-auto.list-item.col-xl-9 > " +
                        "div.page-catalog-accessories__footer > button"
        );

        int maxClicks = 10;
        int clicks = 0;

        while (clicks < maxClicks) {
            try {
                WebDriver driver = getSeleniumSession().getDriver();
                List<WebElement> buttons = driver.findElements(showAllProductBtn);
                if (buttons.isEmpty() || !buttons.get(0).isDisplayed()) {
                    break;
                }

                WebElement button = buttons.get(0);


                WebElement clickable = getSeleniumSession().getWait()
                        .until(ExpectedConditions.elementToBeClickable(button));
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", clickable);

                clicks++;
                log.debug("Клик по 'Показать все товары', клик №{}", clicks);
                Thread.sleep(2000);
            } catch (TimeoutException te) {
                log.info("Кнопка 'Показать все товары' больше не кликабельна, выходим из цикла");
                break;
            } catch (Exception e) {
                log.warn("Ошибка при клике по 'Показать все товары': {}", e.getMessage());
                break;
            }
        }
    }
}