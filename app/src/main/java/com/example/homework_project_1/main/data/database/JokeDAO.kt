package com.example.homework_project_1.main.data.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(joke: JokeDbEntity)

    @Update
    suspend fun update(joke: JokeDbEntity)

    @Delete
    suspend fun delete(joke: JokeDbEntity)

    @Query("SELECT * FROM jokes")
    suspend fun getAllJokes(): List<JokeDbEntity>

    @Query("SELECT * FROM jokes WHERE id = :id")
    suspend fun getJokeById(id: Int): JokeDbEntity

    @Query("SELECT * FROM jokes WHERE isShown = 0 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomJokes(limit: Int): List<JokeDbEntity>

    @Query("SELECT * FROM jokes WHERE source = 'USER'")
    fun getUserJokes(): Flow<List<JokeDbEntity>>

    @Query("DELETE FROM sqlite_sequence WHERE name = 'jokes'")
    suspend fun resetJokesSequence()

    @Query("DELETE FROM jokes")
    suspend fun dropJokesTable()

    @Query("UPDATE jokes SET isShown = :mark WHERE id IN (:ids)")
    suspend fun markShown(mark: Boolean, ids: List<Int>)

    @Query("UPDATE jokes SET isShown = 0")
    suspend fun markUnShown()

    @Query("SELECT DISTINCT category FROM jokes")
    suspend fun getCategories(): List<String>
}