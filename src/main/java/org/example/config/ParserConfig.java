package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Корневой объект конфигурации парсера
 */
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "parser")
public class ParserConfig {

    /**
     * Глобальные настройки Selenium веб‑драйвера.
     */
    private SeleniumConfig selenium = new SeleniumConfig();

    /**
     * Конфигурации сайтов, где ключ — логическое имя сайта
     */
    private Map<String, SiteConfig> sites;

    public SeleniumConfig getSelenium() {
        return selenium;
    }

    public void setSelenium(SeleniumConfig selenium) {
        this.selenium = selenium;
    }

    public Map<String, SiteConfig> getSites() {
        return sites;
    }

    public void setSites(Map<String, SiteConfig> sites) {
        this.sites = sites;
    }
}
