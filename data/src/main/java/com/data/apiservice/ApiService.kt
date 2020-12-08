package com.data.apiservice
import com.data.apiservice.ApiEndPoints.Companion.GET_ALL_MOVIES
import com.data.models.MoviesResponseData
import com.data.models.ResultData
import com.domain.models.MoviesResponseDO
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET(GET_ALL_MOVIES)
    fun getAllMovies(@Query("page") pageNumber:Int): Single<MoviesResponseData>

   }