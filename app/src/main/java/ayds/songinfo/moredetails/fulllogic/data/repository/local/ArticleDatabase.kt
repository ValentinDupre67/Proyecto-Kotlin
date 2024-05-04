package ayds.songinfo.moredetails.fulllogic.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDao
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
}

