package com.example.zappalex.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zappalex.popularmovies.models.Movie;
import com.example.zappalex.popularmovies.utilities.FormatUtils;
import com.example.zappalex.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;



public class MovieDetailActivity extends AppCompatActivity {

    private TextView mMovieTitleTextView;
    private ImageView mMoviePosterImg;
    private TextView mMovieDateTextView;
    private TextView mMovieRatingTextView;
    private TextView mMovieOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // separate layouts exist to accommodate portrait and landscape
        setContentView(R.layout.activity_movie_detail);

        initUiComponents();

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity != null && intentThatStartedActivity.hasExtra(MainActivity.PARCELABLE_MOVIE)) {
            Movie currentMovie = intentThatStartedActivity.getParcelableExtra(MainActivity.PARCELABLE_MOVIE);
            populateViews(currentMovie);
        } else {
            Toast.makeText(this, getString(R.string.msg_movie_detail_error), Toast.LENGTH_LONG).show();
        }
    }

    // this will keep routine ui clutter out of our onCreate()
    private void initUiComponents() {
        mMovieTitleTextView = (TextView) findViewById(R.id.tv_title_movie);
        mMoviePosterImg = (ImageView) findViewById(R.id.img_movie_poster);
        mMovieDateTextView = (TextView) findViewById(R.id.tv_movie_date);
        mMovieRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mMovieOverviewTextView = (TextView) findViewById(R.id.tv_movie_overview);

    }

    private void populateViews(Movie movie) {
        mMovieTitleTextView.setText(movie.getTitle());
        mMovieDateTextView.setText(FormatUtils.getYearFromDateString(movie.getReleaseDate()));
        mMovieRatingTextView.setText(FormatUtils.getFormattedRating(movie.getUserRating()));
        mMovieOverviewTextView.setText(movie.getOverview());

        String picassoImgUrl = NetworkUtils.buildPicassoUrl(movie.getPosterPath());
        Picasso.with(this).load(picassoImgUrl).into(mMoviePosterImg);
    }


}
