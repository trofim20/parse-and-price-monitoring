package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.entity.SeleniumConfig;
import org.example.entity.SiteConfig;
import org.example.repository.SeleniumConfigRepository;
import org.example.repository.SiteConfigRepository;
import org.springframework.stereotype.Component;

/**
 * Корневой объект конфигурации парсера
 */
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParserConfig {

    private final SiteConfigRepository siteConfigRepository;
    private final SeleniumConfigRepository seleniumConfigRepository;

    public SiteConfig getSite(String siteName){
        return siteConfigRepository.findById(siteName)
                .orElseThrow(() -> new IllegalArgumentException("Сайт не найден: " + siteName));
    }

    public List<SiteConfig> getAllEnableSites(){
        return siteConfigRepository.findAllByEnabledTrue();
    }

    public org.example.config.SeleniumConfig getSelenium(){
        SeleniumConfig entity = seleniumConfigRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Настройки selenium не найдены"));
        org.example.config.SeleniumConfig config = new org.example.config.SeleniumConfig();
        config.setUserAgent(entity.getUserAgent());
        config.setWindowSize(entity.getWindowSize());
        config.setPageLoadTimeout(entity.getPageLoadTimeout());
        config.setImplicitWaitTimeout(entity.getImplicitWaitTimeout());
        config.setHeadless(entity.isHeadless());
        return config;
    }
}
