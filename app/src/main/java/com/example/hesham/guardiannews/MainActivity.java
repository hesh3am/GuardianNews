package com.example.hesham.guardiannews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Guardian>> {
    private static String REQUEST_URL = "http://content.guardianapis.com/search?show-tags=contributor&api-key=6b91548c-9e48-4c60-b688-74dda0a68f1b";
    private static final String api_key = "c02a9922-ec3b-41bf-85d2-4124e5832b45";
    private TextView Empty;
    private GuardianAdapter AdapterObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Empty = findViewById(R.id.loading_Text);
        ListView GuardianListView = findViewById(R.id.item_list);
        GuardianListView.setEmptyView(Empty);
        AdapterObject = new GuardianAdapter(this, new ArrayList<Guardian>());

        GuardianListView.setAdapter(AdapterObject);
        GuardianListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Guardian currentNews = AdapterObject.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Uri.Builder uriBuilder = newsUri.buildUpon();
                uriBuilder.appendQueryParameter("api-key", api_key);
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager conctActMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conctActMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            Empty.setText(R.string.ErrorMessage);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Guardian>> loader, List<Guardian> news) {
        View loading = findViewById(R.id.progress_bar);
        loading.setVisibility(View.GONE);
        Empty.setText(R.string.messageerror);
        AdapterObject.clear();

        if (news != null && !news.isEmpty()) {
            AdapterObject.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Guardian>> loader) {
        AdapterObject.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.button_setting) {
            Intent settingsIntent = new Intent(this, GuardianSettings.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Guardian>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minNews = sharedPreferences.getString(getString(R.string.key), getString(R.string.Min_Default));
        String sectionFilter = sharedPreferences.getString(getString(R.string.section_key), getString(R.string.section_default));
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        if (!sectionFilter.equals(getString(R.string.section_default))) {
            uriBuilder.appendQueryParameter("section", sectionFilter);
        }
        uriBuilder.appendQueryParameter("page-size", minNews);
        GuardianLoader loader = new GuardianLoader(this, uriBuilder.toString());
        return loader;
    }
}