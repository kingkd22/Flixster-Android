package com.example.flixster_android

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(
    @SerializedName("title")
    private val rawTitle: String? = null,
    @SerializedName("name")
    private val rawName: String? = null,
    @SerializedName("overview")
    val overview: String = "",
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("release_date")
    private val rawReleaseDate: String? = null,
    @SerializedName("first_air_date")
    private val rawFirstAirDate: String? = null,
    @SerializedName("vote_count")
    val voteCount: Int = 0,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("popularity")
    val popularity: Double = 0.0,
    val isTvShow: Boolean = false
) : Serializable {

    val title: String
        get() = when {
            !rawTitle.isNullOrEmpty() -> rawTitle
            !rawName.isNullOrEmpty() -> rawName
            else -> "Untitled"
        }

    val releaseDate: String?
        get() = rawReleaseDate ?: rawFirstAirDate

    val posterImageUrl: String
        get() = "https://image.tmdb.org/t/p/w500/${posterPath?.removePrefix("/") ?: ""}"

    val backdropImageUrl: String
        get() = "https://image.tmdb.org/t/p/w500/${backdropPath?.removePrefix("/") ?: ""}"
}
