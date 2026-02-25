package org.example.parsers;

import org.example.base.SeleniumBaseParser;
import org.example.config.ParserConfig;
import org.example.service.DetailPageService;
import org.example.service.WebDriverService;
import org.example.util.PageUrlBuilderImpl;
import org.springframework.stereotype.Component;

@Component
public class PlanetAutoParser extends SeleniumBaseParser {
    public PlanetAutoParser(ParserConfig parserConfig,
                            PageUrlBuilderImpl pageUrlBuilder,
                            WebDriverService webDriverService,
                            DetailPageService detailPageService) {
        super("planet-auto",
                parserConfig,
                pageUrlBuilder,
                webDriverService,
                detailPageService);
    }
}
