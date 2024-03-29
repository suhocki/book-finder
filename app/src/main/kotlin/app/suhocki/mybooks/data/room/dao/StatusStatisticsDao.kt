package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.statistics.StatusStatisticsDbo

@Dao
interface StatusStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(statusStatistics: List<StatusStatisticsDbo>)

    @Query("SELECT * FROM StatusStatistics WHERE categoryId=:categoryId ORDER BY count DESC LIMIT 5")
    fun getAllByCategory(categoryId: String): List<StatusStatisticsDbo>
}