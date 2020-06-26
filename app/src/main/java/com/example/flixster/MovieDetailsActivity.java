package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {


    // Movie to display
    Movie movie;
    public static String TRAILER_URL = "https://api.themoviedb.org/3/movie/";

    // Log message to display
    public static final String TAG = "MovieDetailsActivity";

    // The view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    private String youtubeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Standard onCreate setup
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding2 = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view2 = binding2.getRoot();
        setContentView(view2);
        // Resolve the view objects
        tvTitle = (TextView) binding2.tvTitle;
        tvOverview = (TextView) binding2.tvOverview;
        rbVoteAverage = (RatingBar) binding2.rbVoteAverage;
        // Unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        TRAILER_URL = TRAILER_URL+ String.valueOf(movie.getId()) + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        // Set up HTTP client to get the response from the movie database
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TRAILER_URL, new JsonHttpResponseHandler() {

            // When the task is completed successfully, onSuccess is called, and it can give us
            // results to the Logcat regarding details on its success
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                // If our jSon source worked, display results and movies to Logcat
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject tempObj = results.getJSONObject(0);
                    youtubeId = tempObj.getString("key");

                }
                // Else report an exception
                catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }

            }

            // Called when network exception occurred when it talked to server, so report to logcat
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");

            }
        });


        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 =  new Intent(getBaseContext(), MovieTrailerActivity.class);
                intent2.putExtra("ytId", youtubeId);
                startActivity(intent2);
            }
        });

        // Vote average is 0-10, convert to 0-5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }

}