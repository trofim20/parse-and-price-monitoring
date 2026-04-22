package org.example.util;

import org.example.entity.SiteConfig;

public interface PageUrlBuilder {
    String buildPageUrl(String url, SiteConfig paginationParam, int page);
}
