package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.Headers;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Declare class fields which are hardcoded for URL
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";
    List<Movie> movies;

    //onCreate is called when first activity is created....set up views, lists, etc.
    //Activity is initialized and we use setContentView for the UI layout and either findViewbyId
    //or binding to retrieve widgets in UI so we can use code to interact with them.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
        View view = binding.getRoot();
        setContentView(view);

//        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        RecyclerView rvMovies = binding.rvMovies;
        //Set up the list of movies to be displayed
        movies = new ArrayList<>();

        //Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //Set up HTTP client to get the response from the movie database
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {

            //When the task is completed successfully, onSuccess is called, and it can give us
            //results to the Logcat regarding details on its success
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                //If our jSon source worked, display results and movies to Logcat
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());

                }
                //Else report an exception
                catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }

            }

            //Called when network exception occurred when it talked to server, so report to logcat
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");

            }
        });

    }
}