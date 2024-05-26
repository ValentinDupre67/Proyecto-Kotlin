package ayds.songinfo.moredetails.fulllogic.data.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ayds.artist.external.lastfm.data.Source

@Entity
data class CardEntity(
    @PrimaryKey
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: Source,
    val sourceLogoUrl: String
)
