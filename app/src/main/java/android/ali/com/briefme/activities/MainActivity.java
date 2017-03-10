package android.ali.com.briefme.activities;

import android.ali.com.briefme.BriefMeApp;
import android.ali.com.briefme.R;
import android.ali.com.briefme.consts.Urls;
import android.ali.com.briefme.data.OverviewObj;
import android.ali.com.briefme.fragments.NewsFragment;
import android.ali.com.briefme.utils.Utils;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static int REQUESTS_COMPLETED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupPage();

        launchNewsFragment();

        REQUESTS_COMPLETED = 0;
        for (String source: Urls.sources) {
            makeNewsAPIRequest(Urls.buildNewsUrl(source));
        }
    }

    @Override
    protected void onDestroy() {
        BriefMeApp.getInstance().clearObjs();

        if (getSupportFragmentManager().findFragmentById(R.id.flContent) instanceof NewsFragment) {
            NewsFragment frag = (NewsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.flContent);
            frag.clearNewsList();
        }

        super.onDestroy();
    }

    private void launchNewsFragment() {
        try {
            Fragment fragment = (Fragment) NewsFragment.newInstance("");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            Log.e(LOG_TAG, "launchNewsFragment: ", e);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    private void setupPage() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void handleNewsResponse (String response){
        /*
        "status": "ok",
                "source": "cnn",
                "sortBy": "top",
                -
                        "articles": [

        -
                {
                        "author": "Lauren Fox, CNN",
                "title": "GOP tries to discredit agency reviewing health care bill",
                "description": "Republicans don't have a CBO score yet on their health care bill, but they're already taking shots at the CBO.",
                "url": "http://us.cnn.com/2017/03/08/politics/obamacare-cbo/index.html",
                "urlToImage": "http://i2.cdn.cnn.com/cnnnext/dam/assets/170308142145-spicer-0308-super-tease.jpg",
                "publishedAt": "2017-03-08T20:29:59Z"
        }
        */

        try {
            JSONObject responseObj = new JSONObject(response);
            String source = responseObj.getString("source");
            JSONArray articles = responseObj.getJSONArray("articles");
            for (int i = 0; i < articles.length() && i < 5; i++){
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String description = article.getString("description");
                String imageUrl = article.getString("urlToImage");
                String articleUrl = article.getString("url");
                String publishedAt = article.getString("publishedAt");

                OverviewObj obj = new OverviewObj(source, title, description, imageUrl, articleUrl, publishedAt);
                BriefMeApp.getInstance().addObj(obj);
            }
        } catch(Exception e){
            Log.e(LOG_TAG, "handleNewsResponse: ", e);
        }
    }

    public void populateList(OverviewObj obj) {
        if (getSupportFragmentManager().findFragmentById(R.id.flContent) instanceof NewsFragment) {
            NewsFragment frag = (NewsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.flContent);
            frag.addNewsRowToListView(obj);
        }
    }

    public void handleListClick(OverviewObj rowObj){
        launchArticleActivity(rowObj.articleUrl);
    }

    private void launchArticleActivity(String articleUrl) {
        Intent i = new Intent(MainActivity.this, NewsArticleActivity.class);
        i.putExtra(NewsArticleActivity.EXTRA_URL, articleUrl);
        startActivity(i);
    }

    public void checkIfCallsAreDone() {
        REQUESTS_COMPLETED++;

        if(REQUESTS_COMPLETED == Urls.sources.size()) {
            BriefMeApp.getInstance().sortObjs();
            for(OverviewObj obj: BriefMeApp.getInstance().newsObjs) {
                populateList(obj);
            }
        }
    }

    public void makeNewsAPIRequest(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleNewsResponse(response);
                        checkIfCallsAreDone();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errMsg = error.toString();
                if(!Utils.isNetworkAvailable()) {
                    errMsg += " " + BriefMeApp.getInstance().getResources().getString(R.string.no_internet);
                }

                Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_LONG).show();

                Log.e(LOG_TAG, "onErrorResponse: "+ error.toString());
                checkIfCallsAreDone();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
