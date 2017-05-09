package com.sample.popularmovies.Jiny.AysncServices;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Anukool Srivastav on 5/8/2017.
 */

public class TriggerViewEventAsyncTask extends AsyncTask<String, Integer, String> {

    private AsyncResponseInterface responseInterface = null;
    private String viewName = "";

    public TriggerViewEventAsyncTask(AsyncResponseInterface asyncResponseInterface) {
        responseInterface = asyncResponseInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        postData(params[0]);
        return null;
    }

    protected void onPostExecute(String result) {
        responseInterface.onSuccess(viewName);
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    private void postData(String viewId) {
        // Create a new HttpClient and Post Header
        try {
            HttpClient httpclient = new DefaultHttpClient();
            if (viewId != null) {
                HttpGet httpGet = new HttpGet("http://870d5bf4.ngrok.io/" + viewId);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    String jsonString = EntityUtils.toString(httpEntity);
                    JSONObject jObject = new JSONObject(jsonString);
                    viewName = jObject.get("name").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


