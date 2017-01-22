package org.moralsh.android.popularmovies;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.moralsh.android.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    // COMPLETED (12) Create a variable to store a reference to the error message TextView
    private TextView mErrorMessageDisplay;

    // COMPLETED (24) Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_tmdb_query_results_json);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }

    // TODO (1)  (COMPLETADO) Agregar NetworkUtils para gestiona la comunicacion con TheMovieDB
    // TODO (2) Añadir REcyclerView con grid para gestionar la actividad principal de los posters de las películas
    // TODO (3) Generar una actividad nueva para el detalle de las películas

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our {@link TheMovieDBQueryTask}
     */
    private void makePopularMoviesQuery() {
        URL popularMoviesQuery = NetworkUtils.buildPopularUrl();
        mUrlDisplayTextView.setText(popularMoviesQuery.toString());
        new TheMovieDBQueryTask().execute(popularMoviesQuery);
    }

    private void makeTopRatedMoviesQuery() {
        URL topRatedMoviesQuery = NetworkUtils.buildTopRatedUrl();
        mUrlDisplayTextView.setText(topRatedMoviesQuery.toString());
        new TheMovieDBQueryTask().execute(topRatedMoviesQuery);
    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // First, hide the currently visible data
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    public class TheMovieDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL apiURL = params[0];
            String tmdbResults = null;
            try {
                tmdbResults = NetworkUtils.getResponseFromHttpUrl(apiURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmdbResults;
        }

        @Override
        protected void onPostExecute(String tmdbResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mSearchResultsTextView.setText("");
            if (tmdbResults != null && !tmdbResults.equals("")) {
                showJsonDataView();
                try {
                    JSONObject popularMoviesJSON = new JSONObject(tmdbResults);
                    JSONArray resultsArray = popularMoviesJSON.getJSONArray("results");
                    for (int i = 0; i< resultsArray.length(); i++) {
                        JSONObject jsonobject = resultsArray.getJSONObject(i);
                        String title = jsonobject.getString("title");
                        mSearchResultsTextView.append(title + "\n\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // mSearchResultsTextView.setText(tmdbResults);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_popular) {
            makePopularMoviesQuery();
            return true;
        } else if (itemThatWasClickedId == R.id.action_top_rated) {
            makeTopRatedMoviesQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
