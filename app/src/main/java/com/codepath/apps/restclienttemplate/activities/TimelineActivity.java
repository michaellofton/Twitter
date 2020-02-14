package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private final String TAG = TimelineActivity.class.getSimpleName();
    
    private RestClient client;
    private RecyclerView rvTweets;
    private List<Tweet> tweets;
    private TweetsAdapter tweetsAdapter;
    private SwipeRefreshLayout swipeRefreshContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        swipeRefreshContainer = findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeRefreshContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: Fetching new data");
                populateHomeTimeline();
            }
        });

        client = RestApplication.getRestClient(this);
        populateHomeTimeline();

        // Find the RecyclerView
        rvTweets = findViewById(R.id.rvTweets);

        // Initialize the list of tweets and the adapter
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);

        // Configure the RecyclerView: layout manager and adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess: called");
                Log.d(TAG, "onSuccess: "+ json.toString());
                try {
                    tweetsAdapter.clear();
                    tweetsAdapter.addAll(Tweet.fromJsonArray(json.jsonArray));
                    swipeRefreshContainer.setRefreshing(false); //Done using swipe to refresh
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception: ", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure: Exception: " + response + throwable);
            }
        });
    }

    /* crtl + O to open available methods to override*/
    // Called to create the menu buttons on the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; This adds items to the actionbar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; //return true to draw the menu
    }

    // handles menu item clicks on the actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            // Compose menu item was clicked, Start compose activity
            //Coming from this activity to compose activity
            Intent intentComposeTweet = new Intent(this,ComposeActivity.class);
            startActivity(intentComposeTweet);
            return true; // consume the tap of the menu item HERE
        }
        return super.onOptionsItemSelected(item);

    }
}
