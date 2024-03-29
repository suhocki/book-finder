package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.BannerDbo

@Dao
interface BannerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(banners: List<BannerDbo>)

    @Query("SELECT * FROM Banners")
    fun getAll(): List<BannerDbo>
}