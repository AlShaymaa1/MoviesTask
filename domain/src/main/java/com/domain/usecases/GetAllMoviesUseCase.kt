package com.domain.usecases
import com.domain.models.ResultDO
import com.domain.repositories.MovieDao
import io.reactivex.Single
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(private val movieDao: MovieDao?):
    SingleUseCase< List<ResultDO>> {

    override  fun execute(): Single<List<ResultDO>> {
        return movieDao?.getAllMovies()!!
    }
}