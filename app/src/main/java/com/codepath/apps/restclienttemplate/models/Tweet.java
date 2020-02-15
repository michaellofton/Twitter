package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.utils.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    private String body;
    private String createdAt;
    private User user;

    // Empty constructor for parceler
    public Tweet(){
    }

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.setBody(jsonObject.getString("text"));
        tweet.setCreatedAt(jsonObject.getString("created_at"));
        tweet.setUser(User.fromJSON(jsonObject.getJSONObject("user")));
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            list.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public String getFormattedTimestamp() {
        return TimeFormatter.getTimeDifference(createdAt);
    }

    private void setBody(String body) {
        this.body = body;
    }

    private void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }
}

