package com.domain.usecases

import com.domain.models.MoviesResponseDO
import com.domain.repositories.FetchMoviesRepo
import io.reactivex.Single
import javax.inject.Inject

class FetchMoviesUseCase @Inject constructor(private val fetchMoviesRepo: FetchMoviesRepo?):
    GenericUseCase<Int,MoviesResponseDO> {

    override  fun execute(t:Int): Single<MoviesResponseDO> {
        return fetchMoviesRepo?.getMovies(t)!!
    }
}