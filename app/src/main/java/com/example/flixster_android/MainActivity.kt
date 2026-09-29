package com.example.flixster_android

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val POPULAR_TV_URL = "https://api.themoviedb.org/3/tv/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()
    private val tvShows = mutableListOf<Movie>()

    private lateinit var rvMovies: RecyclerView
    private lateinit var rvTvShows: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainView = findViewById<View>(R.id.main)
        val toolbar = findViewById<View>(R.id.toolbar)
        rvMovies = findViewById(R.id.rvMovies)
        rvTvShows = findViewById(R.id.rvTvShows)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            toolbar.setPadding(0, systemBars.top, 0, 0)
            mainView.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val moviesAdapter = MovieAdapter(this, movies, isHorizontalCard = true)
        rvMovies.adapter = moviesAdapter
        rvMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val tvShowsAdapter = MovieAdapter(this, tvShows, isHorizontalCard = true)
        rvTvShows.adapter = tvShowsAdapter
        rvTvShows.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val client = AsyncHttpClient()

        // Fetch Popular Movies
        client.get(POPULAR_MOVIES_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Movies onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d(TAG, "Movies onSuccess: $json")
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    val gson = Gson()
                    val movieType = object : TypeToken<List<Movie>>() {}.type
                    val fetchedMovies: List<Movie> = gson.fromJson(movieJsonArray.toString(), movieType)
                    movies.addAll(fetchedMovies)
                    moviesAdapter.notifyDataSetChanged()
                    Log.d(TAG, "Movies list size ${movies.size}")
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })

        // Fetch Popular TV Shows
        client.get(POPULAR_TV_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "TV Shows onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d(TAG, "TV Shows onSuccess: $json")
                try {
                    val tvJsonArray = json.jsonObject.getJSONArray("results")
                    val gson = Gson()
                    val movieType = object : TypeToken<List<Movie>>() {}.type
                    val fetchedTvShows: List<Movie> = gson.fromJson(tvJsonArray.toString(), movieType)
                    tvShows.addAll(fetchedTvShows)
                    tvShowsAdapter.notifyDataSetChanged()
                    Log.d(TAG, "TV Shows list size ${tvShows.size}")
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })
    }
}
