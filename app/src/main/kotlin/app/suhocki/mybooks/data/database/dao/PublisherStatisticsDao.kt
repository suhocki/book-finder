package app.suhocki.mybooks.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.database.entity.PublisherStatisticsEntity

@Dao
interface PublisherStatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(publisherStatistics: List<PublisherStatisticsEntity>)

    @Query("SELECT * FROM PublisherStatistics WHERE category=:category")
    fun getAllByCategory(category: String): List<PublisherStatisticsEntity>
}