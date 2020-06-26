package com.example.flixster.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;
import org.parceler.Parcels;
import java.util.List;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;
    ItemMovieBinding bindingFinal;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        bindingFinal = ItemMovieBinding.inflate(inflater, parent, false);
        View movieView = bindingFinal.getRoot();
        return new ViewHolder(movieView);
    }


    // Involved populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the view holder
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView)    {
            super(itemView);
            tvTitle = bindingFinal.tvTitle;
            tvOverview = bindingFinal.tvOverview;
            ivPoster = bindingFinal.ivPoster;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e actually exists in the view
            if(position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this wont work if the class is static
                Movie movie = movies.get(position);
                // Create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;

            // Rounded corners setup
            int radius = 30;
            int margin = 10;

            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                // Case 1: If landscape, use landscape placeholder gif to load.
                Glide.with(context).load(imageUrl).transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivPoster);

            }
            else {
                imageUrl = movie.getPosterPath();
                // Case 2: If portrait, use the portrait placeholder gif
                Glide.with(context).load(imageUrl).transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster);
            }

        }
    }
}
