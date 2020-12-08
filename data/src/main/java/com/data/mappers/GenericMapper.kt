package com.data.mappers

import com.domain.models.*
import com.data.models.MoviesResponseData
import com.data.models.ResultData
import javax.inject.Inject

class GenericMapper @Inject constructor() {


    private fun toResultList(data: List<ResultData>): List<ResultDO> {
        return data.map {
            ResultDO(
                it.adult,
                it.backdropPath,
                it.id,
                it.originalLanguage,
                it.originalTitle,
                it.overview,
                it.popularity,
                it.posterPath,
                it.releaseDate,
                it.title,
                it.video,
                it.voteAverage,
                it.voteCount,
                it.isFav
            )
        }
    }


     fun toMoviesResponse(data: MoviesResponseData): MoviesResponseDO {
        return MoviesResponseDO(
             page = data.page,
         results = data.results?.let { toResultList(it) },
         totalPages = data.totalPages,
         totalResults = data.totalResults
        )
    }

}