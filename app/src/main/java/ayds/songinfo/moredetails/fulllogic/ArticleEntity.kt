package ayds.songinfo.moredetails.fulllogic

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArticleEntity(
    @PrimaryKey
    val artistName: String,
    val biography: String,
    val articleUrl: String
)