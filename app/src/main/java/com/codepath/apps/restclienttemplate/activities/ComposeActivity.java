package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    private final String TAG = ComposeActivity.class.getSimpleName();
    
    private EditText etCompose;
    private Button btnTweet;
    TextInputLayout tilCompose;
    private final int minTweetLen = 1;
    private final int maxTweetLen = 140;

    RestClient client;

    //Use Google snackbar for error handling:
    //https://material.io/components/snackbars/#

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Activity created");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        btnTweet = findViewById(R.id.btnTweet);
        etCompose = findViewById(R.id.etCompose);
        tilCompose = findViewById(R.id.tilCompose);
        tilCompose.setCounterMaxLength(maxTweetLen);

        client = RestApplication.getRestClient(this);
        //
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle error
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.length() < minTweetLen ) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, your tweet can't be empty.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else if (tweetContent.length() > maxTweetLen) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, your tweet is too long.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Make an API call to handle the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess: Successfuly published tweet");
                        try {
                            Tweet tweet = Tweet.fromJSON(json.jsonObject);
                            Log.i(TAG, "onSuccess: published tweet: " + tweet.getBody());

                            //Create empty intent to send data
                            Log.d(TAG, "onSuccess: Intent heading for application context");
                            Intent updateTimeline = new Intent(getApplicationContext(), ComposeActivity.class);

                            //Pass tweet back to timeline activity
                            updateTimeline.putExtra("tweet", Parcels.wrap(tweet));

                            //set result code and bundle data for response
                            setResult(RESULT_OK, updateTimeline);
                            Log.d(TAG, "onSuccess: Finishing activity");
                            finish();
                            Log.d(TAG, "onSuccess: Activity finished");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure: Failed to publish tweet", throwable);
                    }
                });

            }
        });
    }
}
