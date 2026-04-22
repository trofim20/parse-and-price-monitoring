package org.example.parsers;

import org.example.base.SeleniumBaseParser;
import org.example.config.ParserConfig;
import org.example.extractor.SeleniumProductCardExtractorImpl;
import org.example.selenium.SeleniumSession;
import org.example.service.DetailPageService;
import org.example.util.PageUrlBuilderImpl;
import org.springframework.stereotype.Component;

@Component
public class VoshodAvtoParser extends SeleniumBaseParser {


    public VoshodAvtoParser(ParserConfig parserConfig, PageUrlBuilderImpl pageUrlBuilder, SeleniumSession seleniumSession, SeleniumProductCardExtractorImpl seleniumProductCardExtractor, DetailPageService detailPageService) {
        super("voshod-avto", parserConfig, pageUrlBuilder, seleniumSession, seleniumProductCardExtractor, detailPageService);
    }
}

