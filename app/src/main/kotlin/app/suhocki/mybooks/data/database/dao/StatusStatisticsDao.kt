package app.suhocki.mybooks.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.database.entity.StatusStatisticsEntity

@Dao
interface StatusStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(statusStatistics: List<StatusStatisticsEntity>)

    @Query("SELECT * FROM StatusStatistics WHERE category=:category")
    fun getAllByCategory(category: String): List<StatusStatisticsEntity>
}