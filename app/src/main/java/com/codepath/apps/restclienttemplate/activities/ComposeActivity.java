package com.codepath.apps.restclienttemplate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.network.RestApplication;
import com.codepath.apps.restclienttemplate.network.RestClient;
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

        //Initialize max lines for edit text based on current configuration:
        Log.d(TAG, "onCreate: setting max config for first time");
        //setMinLines(getResources().getConfiguration());

        //Init rest cilent to make API call
        client = RestApplication.getRestClient(this);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle error
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.length() < minTweetLen ) {
                    displayErrorMessage("Sorry, your tweet can't be 0 characters");
                }
                else if (tweetContent.length() > maxTweetLen) {
                    displayErrorMessage("Sorry, your tweet is too long.");
                }
                else {
                    publishTweet(tweetContent);
                }
            }
        });
    }

    private void displayErrorMessage(String errorMsg) {
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d(TAG, "onConfigurationChanged: called");
        //Change min lines based on new configuration
        setMinLines(newConfig);
    }

    private void setMinLines(Configuration config){
        int minLines = 3; //Default min lines

        //Either the current or new orientation:
        int orientation = config.orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            etCompose.setMinLines(minLines);
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            etCompose.setMinLines(minLines - 1);
        }
        else {
            etCompose.setMinLines(minLines);
        }

    }

    public void publishTweet(String tweetContent){
        // Make an API call to handle the tweet
        client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess: Successfuly published tweet");
                try {
                    Tweet tweet = Tweet.fromJSON(json.jsonObject);

                    //Create empty intent to send data
                    Intent updateTimeline = new Intent(getApplicationContext(), ComposeActivity.class);

                    //Pass tweet back to timeline activity
                    updateTimeline.putExtra("tweet", Parcels.wrap(tweet));

                    //set result code and bundle data for response
                    setResult(RESULT_OK, updateTimeline);
                    finish();

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
}
