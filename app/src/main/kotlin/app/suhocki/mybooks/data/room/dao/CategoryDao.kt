package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.room.*
import app.suhocki.mybooks.data.room.entity.CategoryDbo

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryDbo>)

    @Query("SELECT * FROM Categories LIMIT :limit OFFSET :offset")
    fun getAll(offset: Int, limit: Int): List<CategoryDbo>

    @Query("SELECT * FROM Categories WHERE creationDate=:creationDate")
    fun getAll(creationDate: String): List<CategoryDbo>

    @Query("SELECT * FROM Categories WHERE id=:categoryId")
    fun getCategoryById(categoryId: String): CategoryDbo

    @Delete
    fun deleteAll(categories: List<CategoryDbo>)
}