package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Корневой объект конфигурации парсера
 */
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "parser")
@Getter
@Setter
public class ParserConfig {

    /**
     * Глобальные настройки Selenium веб‑драйвера.
     */
    private SeleniumConfig selenium = new SeleniumConfig();

    /**
     * Конфигурации сайтов, где ключ — логическое имя сайта
     */
    private Map<String, SiteConfig> sites;
}
