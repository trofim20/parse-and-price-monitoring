package org.example.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Product;
import org.example.config.ParserConfig;
import org.example.extractor.SeleniumProductCardExtractorImpl;
import org.example.selenium.SeleniumSession;
import org.example.service.DetailPageService;

import org.example.util.HumanBehaviorUtils;
import org.example.util.PageUrlBuilderImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;



@Slf4j
@Getter
public class SeleniumBaseParser extends AbstractParser {
    private final SeleniumSession seleniumSession;
    private final SeleniumProductCardExtractorImpl seleniumProductCardExtractor;
    private final DetailPageService detailPageService;

    public SeleniumBaseParser(String siteName,
                              ParserConfig parserConfig,
                              PageUrlBuilderImpl pageUrlBuilder,
                              SeleniumSession seleniumSession,
                              SeleniumProductCardExtractorImpl seleniumProductCardExtractor,
                              DetailPageService detailPageService) {
        super(siteName, parserConfig, pageUrlBuilder);
        this.seleniumSession = seleniumSession;
        this.seleniumProductCardExtractor = seleniumProductCardExtractor;
        this.detailPageService = detailPageService;
    }

    @Override
    public LinkedHashSet<Product> parse() {
        LinkedHashSet<Product> products = new LinkedHashSet<>();
        try {
            seleniumSession.start();
            loginIfNeeded();
            int page = 1;

            while (page <= getMaxPage()) {

                String currentUrl = buildPageUrl(page);
                log.debug("парсин страницы " + currentUrl + "для сайта " + siteName);

                navigateTo(currentUrl);

                List<WebElement> productCards = getProductCards();
                if (productCards.isEmpty()) {
                    break;
                }

                for (WebElement productCard : productCards) {
                    try {
                        Product product = parseProductCard(productCard);

                        if (needDetailPage() && product.getUrl() != null) {
                            try {
                                detailPageService.enrichProductFromDetailPage(product, seleniumSession.getDriver(), getSiteConfig());
                                log.info("Парсинг детальной страницы {}" , product.getUrl());
                            }

                            catch (Exception detailEx) {
                                log.warn("Ошибка деталки для {}: {}", product.getName(), detailEx.getMessage());
                            }
                        }
                        if(isValidProduct(product)){
                            products.add(product);

                        }
                    } catch (Exception e) {

                    }
                }
                page++;
                HumanBehaviorUtils.mediumPause();
            }
        } catch (Exception e) {
            log.error("Ошибка при парсинге сайта " + siteName, e);
        } finally {
            seleniumSession.stop();
        }
        return products;
    }

    protected Product parseProductCard(WebElement productCard){
        return seleniumProductCardExtractor.extractProductCard(productCard, getSiteName(),getSiteConfig());
    }

    protected List<WebElement> getProductCards() {
        String cardSelector = getSelector("card");
        try {
            seleniumSession.getWait().until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(cardSelector))
            );
            return seleniumSession.getDriver().findElements(By.cssSelector(cardSelector));
        } catch (Exception first) {

            try {
                ((JavascriptExecutor) seleniumSession.getDriver())
                        .executeScript("window.scrollTo(0, document.body.scrollHeight);");
                sleep(800);

                seleniumSession.getWait().until(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector(cardSelector))
                );
                return seleniumSession.getDriver().findElements(By.cssSelector(cardSelector));

            } catch (Exception second) {
                log.warn("Не удалось найти карточки товаров на странице: {}", siteName, second);
                return new ArrayList<>();
            }
        }
    }


    private void loginIfNeeded() {
        try {
            login();
        } catch (Exception e) {
            log.error("Ошибка при авторизации для {}: {}", siteName, e.getMessage(), e);
        }
    }

    protected void login(){}


    protected void navigateTo(String url) {
        try {
            seleniumSession.getDriver().get(url);

            HumanBehaviorUtils.mediumPause();
            HumanBehaviorUtils.humanScroll(seleniumSession.getDriver());

            afterNavigateTo(url);
        } catch (Exception e) {
            log.warn("Ошибка при переходе на страницу {} для {}", url, siteName, e);
        }
    }


    protected void afterNavigateTo(String url) {
    }
}
