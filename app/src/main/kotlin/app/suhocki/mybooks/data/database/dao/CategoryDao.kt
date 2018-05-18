package app.suhocki.mybooks.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT * from Categories")
    fun getAll(): List<CategoryEntity>
}