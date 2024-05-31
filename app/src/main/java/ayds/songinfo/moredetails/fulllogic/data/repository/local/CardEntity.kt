package ayds.songinfo.moredetails.fulllogic.data.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

@Entity
data class CardEntity(
    @PrimaryKey
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: Int
)
