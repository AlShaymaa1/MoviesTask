package com.moviestask

import android.app.Application
import android.content.Context
import com.data.repos.MovieRepository
import com.moviestask.di.DaggerAppComponent
import com.moviestask.features.database.MovieDatabase
import javax.inject.Inject

class MoviesApplication  @Inject constructor() : Application()  {
    private val database by lazy { MovieDatabase.getDatabase(this) }
    val repository by lazy { MovieRepository(database.movieDao()) }
    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this)?.build()?.inject(this)
        setContext(this.applicationContext)
    }

    companion object {

         private lateinit var context: Context

        fun setContext(con: Context) {
            context=con
        }
    }

    fun getContext():Context{
       return context
    }
}