package org.example.util;

import org.example.entity.SiteConfigEntity;

public interface PageUrlBuilder {
    String buildPageUrl(String url, SiteConfigEntity paginationParam, int page);
}
