package com.example.zappalex.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zappalex.popularmovies.adapters.MovieAdapter;
import com.example.zappalex.popularmovies.models.Movie;
import com.example.zappalex.popularmovies.utilities.FormatUtils;
import com.example.zappalex.popularmovies.utilities.NetworkUtils;
import com.example.zappalex.popularmovies.utilities.TheMovieDbJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final int GRID_LAYOUT_SPAN_PORTRAIT = 2;
    private static final int GRID_LAYOUT_SPAN_LANDSCAPE = 3;

    public static final String PARCELABLE_MOVIE = "parcelable_movie";

    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView movieRecyclerView= (RecyclerView)findViewById(R.id.rv_movie_list);
        mGridLayoutManager = initializeGridLayoutManager();
        movieRecyclerView.setLayoutManager(mGridLayoutManager);

        mMovieAdapter = new MovieAdapter(this);
        movieRecyclerView.setAdapter(mMovieAdapter);

        fetchMoviesOnlyIfDeviceOnline(NetworkUtils.ENDPOINT_POPULAR_MOVIES);
    }

    // in portrait, grid will have 2 columns and in landscape grid will have 3.
    private GridLayoutManager initializeGridLayoutManager(){
        GridLayoutManager gridLayoutManager;
        int deviceOrientation = FormatUtils.getDeviceOrientation(this);

        if(deviceOrientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(this, GRID_LAYOUT_SPAN_PORTRAIT, GridLayoutManager.VERTICAL, false);
        }else{
            gridLayoutManager = new GridLayoutManager(this, GRID_LAYOUT_SPAN_LANDSCAPE, GridLayoutManager.VERTICAL, false);
        }
        return gridLayoutManager;
    }

    // we will only execute the FetchMoviesTask if device online.
    private void fetchMoviesOnlyIfDeviceOnline(String urlEndpoint){
        if (NetworkUtils.isDeviceOnline(this)) {
            new FetchMoviesTask().execute(urlEndpoint);
        }else{
            Toast.makeText(this, getString(R.string.msg_device_offline), Toast.LENGTH_LONG).show();
        }
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            String endpoint = params[0];
            URL movieRequestUrl = NetworkUtils.buildMoviesUrl(endpoint);

            if(movieRequestUrl != null){
                try{
                    String jsonMovieString = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    return TheMovieDbJsonUtils.getMovieListFromJsonString(jsonMovieString);

                }catch(Exception e){
                    e.printStackTrace();
                }
        }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            displayMoviesInGridLayout(movies);
        }
    }

    private void displayMoviesInGridLayout(ArrayList<Movie> moviesList){
        if(moviesList != null){
            mMovieAdapter.setMovieList(moviesList);
            mGridLayoutManager.scrollToPositionWithOffset(0,0);
        }else{
            Toast.makeText(this, getString(R.string.msg_movie_service_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_sort_popular){
            fetchMoviesOnlyIfDeviceOnline(NetworkUtils.ENDPOINT_POPULAR_MOVIES);
            return true;
        }else if(id == R.id.action_sort_top_rated){
            fetchMoviesOnlyIfDeviceOnline(NetworkUtils.ENDPOINT_TOP_RATED_MOVIES);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationActivity = MovieDetailActivity.class;
        Intent intentStartMovieDetail = new Intent(context, destinationActivity);
        intentStartMovieDetail.putExtra(PARCELABLE_MOVIE, movie);

        startActivity(intentStartMovieDetail);
    }


}
