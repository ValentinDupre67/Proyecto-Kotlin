package ayds.songinfo.moredetails.fulllogic.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CardEntity::class], version = 1)
abstract class CardDatabase : RoomDatabase() {
    abstract fun CardDao(): CardDao
}

