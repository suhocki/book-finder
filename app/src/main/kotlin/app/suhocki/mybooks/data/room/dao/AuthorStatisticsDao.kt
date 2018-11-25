package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.statistics.AuthorStatisticsDbo

@Dao
interface AuthorStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(authorStatistics: List<AuthorStatisticsDbo>)

    @Query("SELECT * FROM AuthorStatistics WHERE categoryId=:categoryId ORDER BY count DESC LIMIT 5")
    fun getAllByCategory(categoryId: String): List<AuthorStatisticsDbo>

    @Query("SELECT * FROM AuthorStatistics WHERE categoryId=:categoryId AND author like :searchQuery ORDER BY count DESC")
    fun getAllByNameAndCategory(searchQuery: String, categoryId: String): List<AuthorStatisticsDbo>
}