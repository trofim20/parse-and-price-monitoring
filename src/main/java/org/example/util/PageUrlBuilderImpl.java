package org.example.util;

import org.example.entity.SiteConfig;
import org.springframework.stereotype.Component;


@Component
public class PageUrlBuilderImpl implements PageUrlBuilder {

    @Override
    public String buildPageUrl(String url, SiteConfig pagination, int page) {
        if (page <= 1 || pagination == null) {
            return url;
        }
        String type = pagination.getPaginationType();
        String param = pagination.getPaginationParam();

        if ("param".equalsIgnoreCase(type) && param != null && !param.isBlank()) {
            String sep = url.contains("?") ? "&" : "?";
            if (param.contains("=")) {
                return url + sep + param + page;
            }else {
                return url + sep + param + "=" + page;
            }
        }

        if ("template".equalsIgnoreCase(type) && param != null && !param.isBlank()) {
            String suffix = String.format(param,page);
            if(!url.endsWith("/")) {
                url += "/";
                return url + suffix;
            }
        }
        return url + "page" + page + "/";
    }

}
