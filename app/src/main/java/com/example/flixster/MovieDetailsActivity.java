package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    //Movie to display
    Movie movie;

    //the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //standard onCreate setup
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding binding2 = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        View view2 = binding2.getRoot();

//        setContentView(R.layout.activity_movie_details);

        setContentView(view2);


        //resolve the view objects
        tvTitle = (TextView) binding2.tvTitle;
        tvOverview = (TextView) binding2.tvOverview;
//        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        rbVoteAverage = (RatingBar) binding2.rbVoteAverage;

        //Unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        //vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }
}