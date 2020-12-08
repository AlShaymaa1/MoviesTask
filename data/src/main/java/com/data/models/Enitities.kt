package com.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MoviesResponseData(
    @SerializedName("page")
    @Expose
    var page: Int? = null,

    @SerializedName("results")
    @Expose
    var results: List<ResultData>? = null,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null,

    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

) : Serializable


@Entity(tableName = "movie_table")
data class ResultData(
    @SerializedName("adult")
    @Expose
    @ColumnInfo(name = "adult")
    var adult: Boolean? = null,

    @SerializedName("backdrop_path")
    @Expose
    @ColumnInfo(name = "backdropPath")
    var backdropPath: String? = null,

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @SerializedName("original_language")
    @Expose
    @ColumnInfo(name = "originalLanguage")
    var originalLanguage: String? = null,

    @SerializedName("original_title")
    @Expose
    @ColumnInfo(name = "originalTitle")
    var originalTitle: String? = null,

    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    var overview: String? = null,

    @SerializedName("popularity")
    @Expose
    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,

    @SerializedName("poster_path")
    @Expose
    @ColumnInfo(name = "posterPath")
    var posterPath: String? = null,

    @SerializedName("release_date")
    @Expose
    @ColumnInfo(name = "releaseDate")
    var releaseDate: String? = null,

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    var title: String? = null,

    @SerializedName("video")
    @Expose
    @ColumnInfo(name = "video")
    var video: Boolean? = null,

    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "voteAverage")
    var voteAverage: Double? = null,

    @SerializedName("vote_count")
    @Expose
    @ColumnInfo(name = "voteCount")
    var voteCount: Int? = null,

    @ColumnInfo(name = "isFav")
    var isFav: Boolean? = false

) : Serializable
