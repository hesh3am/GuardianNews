package com.example.hesham.guardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

/**
 * Created by Hesham on 20-Aug-18.
 */

public class GuardianLoader extends AsyncTaskLoader<List<Guardian>> {


    private static final String api_key = "6b91548c-9e48-4c60-b688-74dda0a68f1b";

    String GuardianURL;
    public GuardianLoader(Context context , String url) {
        super(context);
        this.GuardianURL =url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Guardian> loadInBackground() {
        if (GuardianURL == null) {
            return null;
        }
        List<Guardian> guardianList = QueryUtilits.fetchNewsData(GuardianURL);
        return guardianList;
    }
}