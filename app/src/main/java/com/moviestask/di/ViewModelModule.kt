package com.moviestask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moviestask.features.movies.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactoryClean?): ViewModelProvider.Factory?

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: MoviesViewModel?): ViewModel?

}