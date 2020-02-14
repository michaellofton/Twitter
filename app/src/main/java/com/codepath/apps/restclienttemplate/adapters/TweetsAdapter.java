package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private List<Tweet> tweets;
    private Context context;

    // Pass in the context and the list of tweets
    public TweetsAdapter(@NonNull Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row (some constant like 5), inflate the layout (for that tweet)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at the position
        Tweet tweet = tweets.get(position);

        // Bind the tweet with the Viewholder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    /**
     * Used for swipe to refresh
     */
    public void clear() {
        //Delete everything
        tweets.clear(); //Modify existing tweets
        //Update the RecyclerView by telling it that the data changed
        notifyDataSetChanged();
    }
    public void addAll(List<Tweet> tweetList) {
        //Re-add all the tweets (including updated tweets)
        tweets.addAll(tweetList);
        //Update the RecyclerView by telling it that the data changed
        notifyDataSetChanged();
    }


    //Define a view holder
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfileImage;
        private TextView tvScreenName;
        private TextView tvBody;
        private TextView tvTimestamp;

        // Passed in itemView represents 1 row in the RecyclerView
        // (in this case, a tweet)
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);

        }

        private void bind(Tweet tweet) {
            tvScreenName.setText(tweet.getUser().getScreenName());
            tvTimestamp.setText(tweet.getFormattedTimestamp());
            tvBody.setText(tweet.getBody());
            Glide.with(context)
                    .load(tweet.getUser().getPublicImageUrl())
                    .into(ivProfileImage);
        }
    }
}
