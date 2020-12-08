package com.domain.repositories

import com.domain.models.*
import io.reactivex.Single

interface FetchMoviesRepo {

    fun getMovies(pageNumber:Int): Single<MoviesResponseDO>
    }