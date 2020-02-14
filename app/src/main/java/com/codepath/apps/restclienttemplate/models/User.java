package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private String screenName;
    private String publicImageUrl;

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setName(jsonObject.getString("name"));
        user.setScreenName(jsonObject.getString("screen_name"));
        user.setPublicImageUrl(jsonObject.getString("profile_image_url_https"));
        return user;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    private void setPublicImageUrl(String publicImageUrl) {
        this.publicImageUrl = publicImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getPublicImageUrl() {
        return publicImageUrl;
    }
}
