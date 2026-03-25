package org.example.parsers;

import org.example.base.JsoupBaseParser;
import org.example.config.ParserConfig;
import org.example.extractor.JsoupProductCardExtractorImpl;
import org.example.util.PageUrlBuilderImpl;
import org.springframework.stereotype.Component;

@Component
public class AutoTradeParser extends JsoupBaseParser {

    public AutoTradeParser(ParserConfig parserConfig, PageUrlBuilderImpl pageUrlBuilder, JsoupProductCardExtractorImpl jsoupProductCardExtractor) {
        super("auto-trade", parserConfig, pageUrlBuilder, jsoupProductCardExtractor);
    }
}