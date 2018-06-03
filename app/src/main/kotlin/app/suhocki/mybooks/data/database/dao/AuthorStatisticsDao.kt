package app.suhocki.mybooks.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.database.entity.AuthorStatisticsEntity

@Dao
interface AuthorStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(authorStatistics: List<AuthorStatisticsEntity>)

    @Query("SELECT * FROM AuthorStatistics WHERE category=:category")
    fun getAllByCategory(category: String): List<AuthorStatisticsEntity>
}