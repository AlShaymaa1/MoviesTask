package com.data.repos

import androidx.annotation.WorkerThread
import com.domain.models.ResultDO
import com.domain.repositories.MovieDao
import io.reactivex.Single
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieDao: MovieDao) {

    val allMovies: Single<List<ResultDO>> = movieDao.getAllMovies()

    @WorkerThread
     fun insertMovie(resultDO: ResultDO) {
        movieDao.insertMovie(resultDO)
    }
    @WorkerThread
     fun removeMovie(resultDO: ResultDO) {
        movieDao.deleteMovie(resultDO)
    }
}