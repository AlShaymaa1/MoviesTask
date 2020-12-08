package com.moviestask.features.database

import androidx.paging.DataSource
import com.domain.models.ResultDO
import com.domain.usecases.FetchMoviesUseCase
import com.moviestask.features.database.MovieDataSource
import com.moviestask.rx.SchedulersProvider
import com.unicomg.uaa.core.SingleLiveEvent

class MovieDataSourceFactory(

    private val fetchMoviesUseCase: FetchMoviesUseCase,
    private val schedulers: SchedulersProvider,
    private var resultsLiveData: SingleLiveEvent<List<ResultDO>>
) : DataSource.Factory<Int, ResultDO>() {

    val mutableLiveData = SingleLiveEvent<MovieDataSource>()


    override fun create(): DataSource<Int, ResultDO> {
        val userDataSource = MovieDataSource(
            fetchMoviesUseCase,
            schedulers,
            resultsLiveData
        )
        mutableLiveData.postValue(userDataSource)

        return userDataSource
    }
}