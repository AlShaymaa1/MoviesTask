package com.moviestask.features.database

import androidx.paging.PageKeyedDataSource
import com.domain.models.ResultDO
import com.domain.usecases.FetchMoviesUseCase
import com.moviestask.core.Utility.hideProgressBar
import com.moviestask.rx.SchedulersProvider
import com.unicomg.uaa.core.SingleLiveEvent

class MovieDataSource(
    private val fetchMoviesUseCase: FetchMoviesUseCase,
    private val schedulers: SchedulersProvider,
    private val mutableLiveData: SingleLiveEvent<List<ResultDO>>
) : PageKeyedDataSource<Int, ResultDO>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ResultDO>
    ) {
        //if (context.isInternetAvailable()) {
        getMovies(callback)
        //}
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ResultDO>) {
        // if (context.isInternetAvailable()) {
        val nextPageNo = params.key + 1
        getMoreMovies(params.key, nextPageNo, callback)
        // }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ResultDO>) {
        // if (context.isInternetAvailable()) {
        val previousPageNo = if (params.key > 1) params.key - 1 else 0
        getMoreMovies(params.key, previousPageNo, callback)
        //  }
    }

    private fun getMovies(callback: LoadInitialCallback<Int, ResultDO>) {
        // context.showProgressBar()
        fetchMoviesUseCase.execute(1)
            .subscribeOn(schedulers.io())
            .subscribe({
                it?.let {
                    hideProgressBar()
                    mutableLiveData.postValue(it.results)
                    it.results?.let { callback.onResult(it, null, 2) }
                }
            }, {
                hideProgressBar()
            }).let {

            }

    }

    private fun getMoreMovies(
        pageNo: Int,
        previousOrNextPageNo: Int,
        callback: LoadCallback<Int, ResultDO>
    ) {
        //context.showProgressBar()
        fetchMoviesUseCase.execute(pageNo)
            .subscribeOn(schedulers.io())
            .subscribe({
                it?.let {
                    hideProgressBar()
                    mutableLiveData.postValue(it.results)
                    it.results?.let { callback.onResult(it, previousOrNextPageNo) }
                }
            }, {
                hideProgressBar()
            }).let {

            }

    }

}
