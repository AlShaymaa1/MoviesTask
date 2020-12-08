package com.domain.usecases

import io.reactivex.Single

interface GenericUseCase<T,R> {

    fun execute(t:T): Single<R>
}