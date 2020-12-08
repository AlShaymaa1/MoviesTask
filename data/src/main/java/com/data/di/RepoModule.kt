package com.data.di

import com.domain.repositories.*
import com.data.repos.FetchMoviesRepoImpl
import com.data.repos.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class RepoModule {


    @Provides
    @Singleton
    fun providesFetchMoviesRepo(fetchMoviesRepoImpl: FetchMoviesRepoImpl): FetchMoviesRepo? {
        return fetchMoviesRepoImpl
    }
    @Provides
    @Singleton
    fun providesMovieRepo(movieDao: MovieDao) :MovieRepository{
        return MovieRepository(movieDao)
    }

}