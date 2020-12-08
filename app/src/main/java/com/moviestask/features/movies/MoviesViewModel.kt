package com.moviestask.features.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.moviestask.rx.SchedulersProvider
import com.domain.models.ResultDO
import com.domain.usecases.FetchMoviesUseCase
import com.moviestask.MoviesApplication
import com.moviestask.features.database.MovieDataSource
import com.moviestask.features.database.MovieDataSourceFactory
import com.moviestask.features.database.MovieDatabase
import com.unicomg.uaa.core.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject




class MoviesViewModel @Inject constructor(

    private val fetchMoviesUseCase: FetchMoviesUseCase,
    private val schedulers: SchedulersProvider

) : ViewModel() {
    val application=MoviesApplication()
    private val database by lazy { MovieDatabase.getDatabase(application.getContext()) }
   // val repository by lazy { MovieRepository(database.movieDao()) }
    private val compositeDisposable = CompositeDisposable()
    private var listUsers: LiveData<PagedList<ResultDO>> = MutableLiveData<PagedList<ResultDO>>()
    private var mutableLiveData = SingleLiveEvent<MovieDataSource>()
    private var resultsLiveData = SingleLiveEvent<List<ResultDO>>()

    var allFavoriteMoviesLiveData = SingleLiveEvent<List<ResultDO>>()
     var isAddedLiveData = SingleLiveEvent<Boolean>()
     var isRemovedLiveData = SingleLiveEvent<Boolean>()
    init {
        val factory: MovieDataSourceFactory by lazy {
            MovieDataSourceFactory(
                fetchMoviesUseCase,
                schedulers,
                resultsLiveData
            )
        }
        mutableLiveData = factory.mutableLiveData
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()

        listUsers = LivePagedListBuilder(factory, config)
            .build()

    }

    fun getMovies(): LiveData<PagedList<ResultDO>> {

        return listUsers
    }
    fun getList(): SingleLiveEvent<List<ResultDO>> {
        return resultsLiveData
    }

    fun getFavoriteMovies() {
        database.movieDao().getAllMovies()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                it?.let {
                    allFavoriteMoviesLiveData.postValue(it)
                }
            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }

    fun addMovieToFavoriteList(resultDO: ResultDO) {
        Single.fromCallable { database.movieDao().insertMovie(resultDO) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                it?.let {
                    isAddedLiveData.postValue(true)
                }
            }, {
                isAddedLiveData.postValue(false)
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun removeMovieToFavoriteList(resultDO: ResultDO) {
        Single.fromCallable { database.movieDao().deleteMovie(resultDO) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                it?.let {
                    isRemovedLiveData.postValue(true)
                }
            }, {
                isRemovedLiveData.postValue(false)
            }).let {
                compositeDisposable.add(it)
            }
    }


    override fun onCleared() {
        compositeDisposable.clear()
    }

}