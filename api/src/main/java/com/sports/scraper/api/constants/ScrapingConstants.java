package com.sports.scraper.api.constants;

public class ScrapingConstants {

    private ScrapingConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final int CODE_200 = 200;

    public static final String COMMENT_REGEX = "<!--|-->";

    // tags
    public static final String SRC = "src";

    // div
    public static final String DIV = "div";
    public static final String DIV_CONTENT = "div#content";

    // table
    public static final String TABLE_BODY_TAG = "tbody";
    public static final String TABLE_ROW_TAG = "tr";
    public static final String TABLE_DATA_TAG = "td";
    public static final String TABLE_FOOTER = "tfoot";

    // img
    public static final String IMG_ELEMENT = "img[src$=.jpg]";

    // a
    public static final String A_HREF = "a[href]";
    public static final String HREF = "href";

}
