package com.example.homework_project_1.main.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDAO {

    //CRUD operations

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(joke: JokeDbEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCache(joke: JokeCacheEntity)

    @Update
    suspend fun update(joke: JokeDbEntity)

    @Update
    suspend fun updateCache(joke: JokeCacheEntity)

    @Query("DELETE FROM jokes WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM jokesCache WHERE id = :id")
    suspend fun deleteCache(id: Int)

    // Extra operations

    @Query("UPDATE jokes SET isShown = 0")
    suspend fun markUnShown()

    @Query("UPDATE jokesCache SET isShown = 0")
    suspend fun markCacheUnShown()

    @Query("SELECT * FROM jokes WHERE source = 'USER' ")
    suspend fun getAllUserJokes(): List<JokeDbEntity>

    @Query("SELECT COUNT(*) FROM jokes")
    suspend fun getAmountOfJokes(): Int

    @Query("SELECT COUNT(*) FROM jokes WHERE question = :question AND answer = :answer AND category = :category")
    suspend fun checkIfExists(question: String, answer: String, category: String): Int

    @Query("SELECT * FROM jokes")
    suspend fun getAllJokes(): List<JokeDbEntity>

    @Query("SELECT * FROM jokes WHERE id = :id")
    suspend fun getJokeById(id: Int): JokeDbEntity

    @Query("SELECT * FROM jokes WHERE isShown = 0 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomJokes(limit: Int): List<JokeDbEntity>

    @Query("SELECT * FROM jokes WHERE source = 'USER' AND createdAt > :lastTimestamp AND isShown = 0 ORDER BY createdAt ASC")
    fun getUserJokesAfter(lastTimestamp: Long): Flow<List<JokeDbEntity>>

    @Query("DELETE FROM sqlite_sequence WHERE name = 'jokes'")
    suspend fun resetJokesSequence()

    @Query("DELETE FROM jokes")
    suspend fun dropJokesTable()

    @Query("UPDATE jokes SET isShown = :mark WHERE id IN (:ids)")
    suspend fun markShown(mark: Boolean, ids: List<Int>)

    @Query("SELECT DISTINCT category FROM jokes")
    suspend fun getCategories(): List<String>

    @Query("UPDATE jokes SET isFavourite = :isFavourite WHERE id = :jokeId")
    suspend fun updateFavouriteStatus(jokeId: Int, isFavourite: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM jokes WHERE id = :id AND category = :category AND question = :question AND answer = :answer)")
    suspend fun isJokeExists(id: Int, category: String, question: String, answer: String,): Boolean

    //Cache
    @Query("SELECT COUNT(*) FROM jokesCache WHERE question = :question AND answer = :answer AND category = :category")
    suspend fun checkIfCacheExists(question: String, answer: String, category: String): Int

    @Query("UPDATE jokesCache SET isShown = :mark WHERE id IN (:ids)")
    suspend fun markCacheShown(mark: Boolean, ids: List<Int>)

    @Query("UPDATE jokesCache SET isShown = 1")
    suspend fun markCacheShown()

    @Query("SELECT * FROM jokesCache WHERE isShown = 0 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomCacheJokes(limit: Int): List<JokeCacheEntity>

    @Query("SELECT * FROM jokesCache")
    suspend fun getAllCacheJokes(): List<JokeCacheEntity>

    @Query("SELECT * FROM jokesCache WHERE createdAt < :lastTimestamp ORDER BY createdAt ASC")
    suspend fun getCachedJokesBefore(lastTimestamp: Long): List<JokeDbEntity>
}
