package com.moviestask.di

import android.app.Application
import com.data.di.NetworkModule
import com.data.di.RepoModule
import com.moviestask.MainActivity
import com.moviestask.MoviesApplication
import com.moviestask.features.favorites.FavoritesFragment
import com.moviestask.features.moviedetails.MovieDetailsFragment
import com.moviestask.features.movies.ListMoviesFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepoModule::class, AppModule::class, AndroidSupportInjectionModule::class, ViewModelModule::class, ActivityBindingModule::class])
interface AppComponent : AndroidInjector<MoviesApplication?> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder?
        fun build(): AppComponent?
    }

    fun inject(homeActivity: MainActivity?)
    fun inject(listMoviesFragment: ListMoviesFragment?)
    fun inject(favoritesFragment: FavoritesFragment?)
    fun inject(movieDetailsFragment: MovieDetailsFragment?)


}
