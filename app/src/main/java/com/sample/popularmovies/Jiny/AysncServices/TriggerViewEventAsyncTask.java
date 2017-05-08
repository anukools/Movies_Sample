package com.sample.popularmovies.Jiny.AysncServices;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Anukool Srivastav on 5/8/2017.
 */

public class TriggerViewEventAsyncTask extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        postData(params[0]);
        return null;
    }

    protected void onPostExecute(String result) {
        Log.e("Data : ", result + "");
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    private void postData(String viewTrigger) {
        // Create a new HttpClient and Post Header
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://83a344a7.ngrok.io/2");
            if (viewTrigger != null) {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpGet);
                Log.e("Response  - ", response.toString() + "");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


