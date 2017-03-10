package android.ali.com.briefme.activities;

import android.ali.com.briefme.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsArticleActivity extends AppCompatActivity {

    private String articleUrl = "http://www.cnn.com";
    private WebView webview;

    public static final String EXTRA_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.getString(EXTRA_URL) != null) {
                articleUrl = extras.getString(EXTRA_URL);
            }
        }

        setWebview();
    }

    private void setWebview() {
        webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl(articleUrl);

        // Enable Javascript
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webview.setWebViewClient(new WebViewClient());
    }
}
