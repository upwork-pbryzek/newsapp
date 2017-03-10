package android.ali.com.briefme.consts;

import java.util.ArrayList;

/**
 * Created by Paul on 3/2/17.
 */

public class Urls {

    public static ArrayList<String> sources;

    public static enum RequestType {
        TRANSLATE,
        TRANSLATE_OCR,
        LANGS
    }

    //News Constants
    public static final String NEWS_API_KEY = "b77f4061fdde40508a764f163efadac3";
    public static final String NEWS_DOMAIN = "https://newsapi.org/v1/articles";

    public static final String SOURCE_BBC = "bbc-news";
    public static final String SOURCE_BI = "business-insider";
    public static final String SOURCE_BLOOMBERG = "bloomberg";
    public static final String SOURCE_FORTUNE = "fortune";
    public static final String SOURCE_NEWSWEEK = "newsweek";
    public static final String SOURCE_REUTERS = "reuters";
    public static final String SOURCE_HUFFINGTON = "the-huffington-post";
    public static final String SOURCE_NYT = "the-new-york-times";
    public static final String SOURCE_WALL_STREET = "the-wall-street-journal";
    public static final String SOURCE_TIME = "time";

    static {
        sources = new ArrayList<String>();
        sources.add(SOURCE_BBC);
        sources.add(SOURCE_BI);
        sources.add(SOURCE_BLOOMBERG);
        sources.add(SOURCE_FORTUNE);
        sources.add(SOURCE_NEWSWEEK);
        sources.add(SOURCE_REUTERS);
        sources.add(SOURCE_HUFFINGTON);
        sources.add(SOURCE_NYT);
        sources.add(SOURCE_WALL_STREET);
        sources.add(SOURCE_TIME);
    }

    public static String buildNewsUrl(String source) {
        String url = NEWS_DOMAIN + "?source=" + source + "&sortBy=top&apiKey=" + NEWS_API_KEY;

        return url;
    }
}
