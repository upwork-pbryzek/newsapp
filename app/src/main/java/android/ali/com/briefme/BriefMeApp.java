package android.ali.com.briefme;

import android.ali.com.briefme.data.OverviewObj;
import android.app.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Paul on 3/8/17.
 */

public class BriefMeApp extends Application {

    private static final String LOG_TAG = BriefMeApp.class.getName();

    // Singleton instance
    private static BriefMeApp sInstance = null;

    public ArrayList<OverviewObj> newsObjs;

    private String userLang;
    private String transLang;

    public String getUserLang() {
        return userLang;
    }

    public void setUserLang(String userLang) {
        this.userLang = userLang;
    }

    public String getTransLang() {
        return transLang;
    }

    public void setTransLang(String transLang) {
        this.transLang = transLang;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Setup singleton instance
        sInstance = this;

        newsObjs = new ArrayList<OverviewObj>();
    }

    public void clearObjs() {
        newsObjs.clear();
    }

    public void addObj(OverviewObj obj) {
        newsObjs.add(obj);
    }

    public void sortObjs() {
        Collections.sort(newsObjs, new DateComparator());
    }

    public class DateComparator implements Comparator<OverviewObj> {
        public int compare(OverviewObj lhs, OverviewObj rhs) {
            return rhs.publishedDate.compareTo(lhs.publishedDate);
        }
    }

    // Getter to access Singleton instance
    public static BriefMeApp getInstance() {
        return sInstance ;
    }
}