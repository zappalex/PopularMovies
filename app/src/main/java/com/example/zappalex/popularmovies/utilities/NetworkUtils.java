package com.example.zappalex.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by user on 6/14/17.
 * This is a util class that handles operations related to networking and
 * fetching information from the API
 */

public class NetworkUtils {

    // Keys - add API key here
    private static final String API_KEY = "";

    // URL Paths
    private static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3/movie";
    private static final String BASE_PICASSO_URL = "http://image.tmdb.org/t/p/";
    public static final String ENDPOINT_POPULAR_MOVIES = "popular";
    public static final String ENDPOINT_TOP_RATED_MOVIES = "top_rated";

    // Query Paths
    private static final String API_KEY_PARAM = "api_key";

    // Picasso sizes ( only one for now )
    private static final String IMG_SIZE_W342 = "w342";


    public static URL buildMoviesUrl(String endpoint) {
        Uri builtUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(endpoint)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        return  convertUriToUrl(builtUri);
    }

    // build uri then return correct url
    private static URL convertUriToUrl(Uri uriToConvert) {
        URL url = null;
        try {
            url = new URL(uriToConvert.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // return a formatted picasso url
    public static String buildPicassoUrl(String posterPath) {
        return BASE_PICASSO_URL + IMG_SIZE_W342 + "/" + posterPath;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // check if device is online
    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
