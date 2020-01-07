package com.jndcb.jndcbtwitter.Controller;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jndcb.jndcbtwitter.MainActivity;
import com.jndcb.jndcbtwitter.Model.Tweet;
import com.jndcb.jndcbtwitter.Model.TwitterApiInterface;
import com.jndcb.jndcbtwitter.R;
import com.jndcb.jndcbtwitter.Ui.TwitterListFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SearchTwitterController {

    private ArrayList<Tweet> tweetsList;
    private ProgressDialog dialog;
    MainActivity mainActivity;
    TwitterApiInterface twitterApiInterface;
    String getSearchText;

    public SearchTwitterController(MainActivity main, TwitterApiInterface twitterApi, String text) {
        twitterApiInterface = twitterApi;
        getSearchText = text;
        mainActivity = main;
        dialog = new ProgressDialog(main);

        startGetList();
    }


    private void startGetList () {
        dialog = ProgressDialog.show(MainActivity.getGlobalContext(), "", MainActivity.getGlobalContext().getString(R.string.searching_text));

        tweetsList = new ArrayList<>();
        twitterApiInterface.getUserTimeline(getSearchText).enqueue(userTimelineCallback);
    }

    Callback<List<Tweet>> userTimelineCallback  = new Callback<List<Tweet>>() {
        @Override
        public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
            if (response.isSuccessful()) {
                tweetsList = (ArrayList<Tweet>) response.body();

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getGlobalContext());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(tweetsList);

                editor.putString(TAG, json);
                editor.apply();

                mainActivity.loadFragment(new TwitterListFragment());

                dialog.dismiss();

                Log.d("xxxxx", String.valueOf(tweetsList));
            } else {
                Toast.makeText(mainActivity.getApplicationContext(), MainActivity.getGlobalContext().getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<List<Tweet>> call, Throwable t) {
            Log.d("SearchUser", String.valueOf(t.fillInStackTrace()));
        }

    };
}
