package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.room.entity.ShopInfoDbo

@Dao
interface ShopInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shopInfo: ShopInfoDbo)

    @Query("SELECT * FROM ShopInfo LIMIT 1")
    fun get(): ShopInfoDbo?
}