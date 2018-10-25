package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.PublisherStatisticsEntity

@Dao
interface PublisherStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(publisherStatistics: List<PublisherStatisticsEntity>)

    @Query("SELECT * FROM PublisherStatistics WHERE category=:category ORDER BY count DESC LIMIT 5")
    fun getAllByCategory(category: String): List<PublisherStatisticsEntity>

    @Query("SELECT * FROM PublisherStatistics WHERE category=:category AND publisher like :searchQuery ORDER BY count DESC")
    fun getAllByNameAndCategory(searchQuery: String, category: String): List<PublisherStatisticsEntity>
}