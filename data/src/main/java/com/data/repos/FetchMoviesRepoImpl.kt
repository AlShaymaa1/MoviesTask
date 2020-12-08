package com.data.repos

import com.data.apiservice.ApiService
import com.data.mappers.GenericMapper
import com.domain.models.*
import com.domain.repositories.FetchMoviesRepo
import com.domain.repositories.MovieDao
import io.reactivex.Single
import javax.inject.Inject

class FetchMoviesRepoImpl @Inject constructor(
    private val apiService: ApiService?,
    private val shareMapper: dagger.Lazy<GenericMapper>
) : FetchMoviesRepo {

    override fun getMovies(pageNumber:Int): Single<MoviesResponseDO> {
        return apiService?.getAllMovies(pageNumber)
            ?.map {
                shareMapper.get().toMoviesResponse(it)
            }!!
    }


}