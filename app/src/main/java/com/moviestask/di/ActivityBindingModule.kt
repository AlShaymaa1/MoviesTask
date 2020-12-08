package com.moviestask.di

import com.moviestask.MainActivity
import com.moviestask.di.scope.ActivityScope
import com.moviestask.di.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [ViewModelModule::class, AndroidSupportInjectionModule::class])
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainScreenActivity(): MainActivity?
}