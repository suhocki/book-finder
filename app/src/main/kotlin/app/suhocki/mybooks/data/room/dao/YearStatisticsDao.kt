package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.YearStatisticsEntity

@Dao
interface YearStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(yearStatistics: List<YearStatisticsEntity>)

    @Query("SELECT * FROM YearStatistics WHERE category=:category ORDER BY year DESC")
    fun getAllByCategory(category: String): List<YearStatisticsEntity>
}