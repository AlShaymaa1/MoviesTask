package com.domain.usecases

import com.domain.models.ResultDO
import com.domain.repositories.MovieDao
import javax.inject.Inject

class InsertMovieUseCase  @Inject constructor(private val movieDao: MovieDao?):
    MovieUseCas<ResultDO> {

    override  fun execute(t: ResultDO) {
        return movieDao?.insertMovie(t)!!
    }
}