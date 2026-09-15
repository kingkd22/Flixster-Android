package com.example.flixster_android

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String
) {
    val posterImageUrl: String
        get() = "https://image.tmdb.org/t/p/w500/$posterPath"

    val backdropImageUrl: String
        get() = "https://image.tmdb.org/t/p/w500/$backdropPath"
}
