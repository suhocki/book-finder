package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.statistics.PriceStatisticsEntity

@Dao
interface PriceStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(priceStatistics: List<PriceStatisticsEntity>)

    @Query("SELECT * FROM PriceStatistics WHERE categoryId=:categoryId")
    fun getAllByCategory(categoryId: String): PriceStatisticsEntity
}