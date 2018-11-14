package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.statistics.PublisherStatisticsEntity

@Dao
interface PublisherStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(publisherStatistics: List<PublisherStatisticsEntity>)

    @Query("SELECT * FROM PublisherStatistics WHERE categoryId=:categoryId ORDER BY count DESC LIMIT 5")
    fun getAllByCategory(categoryId: String): List<PublisherStatisticsEntity>

    @Query("SELECT * FROM PublisherStatistics WHERE categoryId=:categoryId AND publisher like :searchQuery ORDER BY count DESC")
    fun getAllByNameAndCategory(searchQuery: String, categoryId: String): List<PublisherStatisticsEntity>
}