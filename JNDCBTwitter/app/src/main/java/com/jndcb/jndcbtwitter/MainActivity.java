package com.jndcb.jndcbtwitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jndcb.jndcbtwitter.Controller.SearchTwitterController;
import com.jndcb.jndcbtwitter.Model.OAuth2Token;
import com.jndcb.jndcbtwitter.Model.TwitterApiInterface;
import com.jndcb.jndcbtwitter.Utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String credentials = Credentials.basic("7CRv5MEiDWgzoWBfyuz3bbO0f", "wQQrLxk5CCgFN02rF7z8NBmvD8LtDWBPORVQJB7aGsS7CRbNNN");

    private List<String> sampleList = new ArrayList<>();

    private static Context context;
    EditText editTextSearch;
    Button buttonSearch;
    ImageView imageViewTwitter;
    public TwitterApiInterface twitterApiInterface;
    public OAuth2Token token;

    /**
     * Retrieve global application context.
     *
     * @return
     */
    public static Context getGlobalContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        editTextSearch = findViewById(R.id.text_field);
        buttonSearch = findViewById(R.id.button);
        imageViewTwitter = findViewById(R.id.image_twitter);


        buttonSearch.setBackgroundColor(getResources().getColor(R.color.twitterButton));
        editTextSearch.setHorizontallyScrolling(false);
        editTextSearch.setMaxLines(1);
        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                }
            }
        });

        createTwitterApi();
    }

    private void createTwitterApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        token != null ? token.getAuthorization() : credentials);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TwitterApiInterface.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        twitterApiInterface = retrofit.create(TwitterApiInterface.class);

        getToken();
    }


    private void getToken () {
        twitterApiInterface.postCredentials("client_credentials").enqueue(tokenCallback);

    }

    Callback<OAuth2Token> tokenCallback = new Callback<OAuth2Token>() {
        @Override
        public void onResponse(Call<OAuth2Token> call, Response<OAuth2Token> response) {
            if (response.isSuccessful()) {
                token = response.body();
            } else {
                Toast.makeText(MainActivity.this, "Failure while requesting token", Toast.LENGTH_LONG).show();
                Log.d("RequestTokenCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<OAuth2Token> call, Throwable t) {
            t.printStackTrace();
        }
    };

    public void CallFragment(View view) {
        String getText = editTextSearch.getText().toString();
        if (!getText.isEmpty()) {
            new SearchTwitterController(MainActivity.this, twitterApiInterface, getText);
        }
        else {
            new Util().AlertMessage(getResources().getString(R.string.edittext_empty_field), context);
        }
    }

    public void loadFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = this.getSupportFragmentManager();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        else {
            new Util().AlertMessageTwoButtons(getResources().getString(R.string.exit_app), context);
        }
    }

    // https://stackoverflow.com/questions/20713273/dismiss-keyboard-when-click-outside-of-edittext-in-android
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scRecords[] = new int[2];
            v.getLocationOnScreen(scRecords);
            float x = ev.getRawX() + v.getLeft() - scRecords[0];
            float y = ev.getRawY() + v.getTop() - scRecords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        InputMethodManager input = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (input != null) {
            input.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
            findViewById(android.R.id.content).clearFocus();
        }
    }
}
