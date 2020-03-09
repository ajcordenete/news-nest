package com.aljon.newsnest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aljon.newsnest.model.DatabaseArticle

@Database(entities = [DatabaseArticle::class], version = 3)
abstract class NewsDatabase: RoomDatabase() {
    abstract val articleDao: ArticleDao

    companion object  {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private lateinit var INSTANCE: NewsDatabase

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getDatabase(context: Context): NewsDatabase {

            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // If instance is `null` make a new database instance.
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NewsDatabase::class.java,
                        "news_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return INSTANCE
            }
        }
    }
}