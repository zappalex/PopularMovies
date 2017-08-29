package com.example.zappalex.popularmovies.utilities;

import com.example.zappalex.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 6/14/17.
 * This is a util class for parsing the json data returned from the API into our Movie object.
 */

public class TheMovieDbJsonUtils {

    // The Movie Database (TMDB) json keys
    private static final String TMDB_RESULTS = "results";
    private static final String TMDB_TITLE = "title";
    private static final String TMDB_POSTER_PATH = "poster_path";
    private static final String TMDB_OVERVIEW = "overview";
    private static final String TMDB_USER_RATING = "vote_average";
    private static final String TMDB_RELEASE_DATE = "release_date";

    public static ArrayList<Movie> getMovieListFromJsonString(String jsonMovieString) throws JSONException {

        ArrayList<Movie> movieList = new ArrayList<>();
        JSONObject jsonMoviesObject = new JSONObject(jsonMovieString);

        JSONArray moviesArray = jsonMoviesObject.getJSONArray(TMDB_RESULTS);

        // Consider parsing status codes here if additional handling needed.  For now we return null if exception.

        for(int i=0; i<moviesArray.length(); i++){
            Movie currentMovie = new Movie();
            JSONObject jsonMovie = moviesArray.getJSONObject(i);

            currentMovie.setTitle(jsonMovie.getString(TMDB_TITLE));
            currentMovie.setPosterPath(jsonMovie.getString(TMDB_POSTER_PATH));
            currentMovie.setOverview(jsonMovie.getString(TMDB_OVERVIEW));
            currentMovie.setUserRating(jsonMovie.getString(TMDB_USER_RATING));
            currentMovie.setReleaseDate(jsonMovie.getString(TMDB_RELEASE_DATE));

            movieList.add(currentMovie);
        }

        return movieList;
    }


}
