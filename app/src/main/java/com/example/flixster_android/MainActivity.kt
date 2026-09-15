package com.example.flixster_android

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rvMovies)

        val movieAdapter = MovieAdapter(this, movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d(TAG, "onSuccess: JSON data $json")
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    val gson = Gson()
                    val movieType = object : TypeToken<List<Movie>>() {}.type
                    val fetchedMovies: List<Movie> = gson.fromJson(movieJsonArray.toString(), movieType)
                    movies.addAll(fetchedMovies)
                    movieAdapter.notifyDataSetChanged()
                    Log.d(TAG, "Movie list size ${movies.size}")
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })
    }
}
