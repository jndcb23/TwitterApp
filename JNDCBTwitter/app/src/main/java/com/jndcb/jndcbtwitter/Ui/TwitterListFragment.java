package com.jndcb.jndcbtwitter.Ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jndcb.jndcbtwitter.Adapter.TweetAdapter;
import com.jndcb.jndcbtwitter.MainActivity;
import com.jndcb.jndcbtwitter.Model.Tweet;
import com.jndcb.jndcbtwitter.R;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.ContentValues.TAG;

public class  TwitterListFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.twitter_fragment, container, false);

        ListView listView =rootView.findViewById(R.id.list);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getGlobalContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, "");
        Type type = new TypeToken<List<Tweet>>() {}.getType();
        List<Tweet> arrayList = gson.fromJson(json, type);

        listView.setAdapter(new TweetAdapter(MainActivity.getGlobalContext(), arrayList));

        return rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences settings =  PreferenceManager.getDefaultSharedPreferences(MainActivity.getGlobalContext());
        settings.edit().clear().apply();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
