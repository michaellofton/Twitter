package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.google.android.material.textfield.TextInputLayout;

public class ComposeActivity extends AppCompatActivity {

    private EditText etCompose;
    private Button btnTweet;
    TextInputLayout tilCompose;
    private final int minTweetLen = 1;
    private final int maxTweetLen = 140;

    //Use Google snackbar for error handling:
    //https://material.io/components/snackbars/#

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        btnTweet = findViewById(R.id.btnTweet);
        etCompose = findViewById(R.id.etCompose);
        tilCompose = findViewById(R.id.tilCompose);
        tilCompose.setCounterMaxLength(maxTweetLen);

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
                }
                else if (tweetContent.length() > maxTweetLen) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, your tweet is too long.",
                            Toast.LENGTH_LONG).show();
                }
                else {

                }
                // Make an API call to handle the tweet

            }
        });
    }
}
