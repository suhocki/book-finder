package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.*
import app.suhocki.mybooks.data.room.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT * from Categories")
    fun getAll(): List<CategoryEntity>

    @Query("SELECT * from Categories WHERE id=:categoryId")
    fun getCategoryById(categoryId: String): CategoryEntity

    @Delete
    fun deleteAll(categories: List<CategoryEntity>)
}