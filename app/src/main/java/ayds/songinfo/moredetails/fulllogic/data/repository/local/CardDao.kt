package ayds.songinfo.moredetails.fulllogic.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardEntity)

    @Query("SELECT * FROM Cardentity WHERE artistName LIKE :artistName AND source LIKE :source LIMIT 1")
    fun getCardByArtistNameAndSource(artistName: String, source: String): CardEntity?

}