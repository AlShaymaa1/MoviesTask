package com.domain.usecases

import io.reactivex.Single

interface MovieUseCas<T> {
    fun execute(t:T)
}