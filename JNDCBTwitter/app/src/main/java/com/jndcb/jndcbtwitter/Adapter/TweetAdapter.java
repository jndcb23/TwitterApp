package com.jndcb.jndcbtwitter.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jndcb.jndcbtwitter.Model.Tweet;
import com.jndcb.jndcbtwitter.R;

import java.util.List;

public class TweetAdapter extends BaseAdapter {

    List<Tweet> tweetList;
    Context context;

    public TweetAdapter(Context context, List<Tweet> tweetList) {
        this.tweetList = tweetList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (tweetList == null) ? 0 : tweetList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.tweet_list_item, null);
        }

        Tweet tweet = tweetList.get(position);
        TextView tweetTxt = convertView.findViewById(R.id.tweetText);
        TextView tweetBy = convertView.findViewById(R.id.tweetByUser);

        tweetTxt.setText(tweet.getTweet());
        tweetBy.setText(tweet.getTweetBy());

        return convertView;
    }
}
