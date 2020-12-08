package com.domain.repositories


import android.arch.persistence.room.*
import com.domain.models.ResultDO
import io.reactivex.Single
import javax.inject.Inject


@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table ")
    fun getAllMovies(): Single<List<ResultDO>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: ResultDO)

    @Delete
    fun deleteMovie(movie: ResultDO)
}