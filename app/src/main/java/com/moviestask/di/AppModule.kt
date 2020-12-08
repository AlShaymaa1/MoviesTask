package com.moviestask.di

import android.app.Application
import android.content.Context
import androidx.annotation.Nullable
import com.moviestask.MoviesApplication
import com.moviestask.rx.SchedulersFacade
import com.moviestask.rx.SchedulersProvider
import dagger.Binds
import dagger.Module
import org.jetbrains.annotations.NotNull
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds

    abstract fun bindContext(application: MoviesApplication?): Context?
    @Binds
    abstract fun providerScheduler(schedulersFacade: SchedulersFacade?): SchedulersProvider?

}
