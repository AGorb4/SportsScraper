package com.sports.scraper.api.utils;

import org.jsoup.nodes.Element;

import com.sports.scraper.api.constants.ScrapingConstants;

public class MapperUtils {

    private MapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static float getFloat(String text) {
        if (text != null && text.length() > 0) {
            return Float.parseFloat(text);
        }
        return 0.0f;
    }

    public static double getDouble(String text) {
        if (text != null && text.length() > 0) {
            return Double.parseDouble(text);
        }
        return 0.0;
    }

    public static int getInteger(String text) {
        if (text != null && text.length() > 0) {
            return Integer.parseInt(text);
        }
        return 0;
    }

    public static String getElementUrl(Element column) {
        return column.select(ScrapingConstants.A_HREF).first().attr(ScrapingConstants.HREF);
    }
}
