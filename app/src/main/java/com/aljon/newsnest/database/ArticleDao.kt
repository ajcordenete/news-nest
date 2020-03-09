package com.aljon.newsnest.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aljon.newsnest.model.DatabaseArticle

@Dao
interface ArticleDao {

    @Insert
    fun insert(article: DatabaseArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg articles: DatabaseArticle)

    @Query("Select * from database_article_table")
    fun getAllArticles(): LiveData<List<DatabaseArticle>>

    @Query("Delete from database_article_table")
    fun deleteAll()

    @Query("Select * from database_article_table where isSaved = 1")
    fun getSavedArticles(): LiveData<List<DatabaseArticle>>
}