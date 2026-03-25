package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.entity.SeleniumConfigEntity;
import org.example.entity.SiteConfigEntity;
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

    public SiteConfigEntity getSite(String siteName){
        return siteConfigRepository.findById(siteName)
                .orElseThrow(() -> new IllegalArgumentException("Сайт не найден: " + siteName));
    }

    public List<SiteConfigEntity> getAllEnableSites(){
        return siteConfigRepository.findAllByEnabledTrue();
    }

    public SeleniumConfig getSelenium(){
        SeleniumConfigEntity entity = seleniumConfigRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Настройки selenium не найдены"));
        SeleniumConfig config = new SeleniumConfig();
        config.setUserAgent(entity.getUserAgent());
        config.setWindowSize(entity.getWindowSize());
        config.setPageLoadTimeout(entity.getPageLoadTimeout());
        config.setImplicitWaitTimeout(entity.getImplicitWaitTimeout());
        config.setHeadless(entity.isHeadless());
        return config;
    }
}
