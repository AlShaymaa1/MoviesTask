package com.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class MoviesResponseDO (

    var page: Int? = null,
    var results: List<ResultDO>? = null,
    var totalPages: Int? = null,
    var totalResults: Int? = null
):Serializable


@Entity(tableName = "movie_table")
data class ResultDO (
    @ColumnInfo(name = "adult")
    var adult: Boolean? = null,
    @ColumnInfo(name = "backdropPath")
    var backdropPath: String? = null,
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Int? = null,
    @ColumnInfo(name = "originalLanguage")
    var originalLanguage: String? = null,
    @ColumnInfo(name = "originalTitle")
    var originalTitle: String? = null,
    @ColumnInfo(name = "overview")
    var overview: String? = null,
    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,
    @ColumnInfo(name = "posterPath")
    var posterPath: String? = null,
    @ColumnInfo(name = "releaseDate")
    var releaseDate: String? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "video")
    var video: Boolean? = null,
    @ColumnInfo(name = "voteAverage")
    var voteAverage: Double? = null,
    @ColumnInfo(name = "voteCount")
    var voteCount: Int? = null,
@ColumnInfo(name = "isFav")
var isFav: Boolean? = false
):Serializable

