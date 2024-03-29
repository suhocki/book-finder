package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.statistics.YearStatisticsDbo

@Dao
interface YearStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(yearStatistics: List<YearStatisticsDbo>)

    @Query("SELECT * FROM YearStatistics WHERE categoryId=:categoryId ORDER BY year DESC")
    fun getAllByCategory(categoryId: String): List<YearStatisticsDbo>
}