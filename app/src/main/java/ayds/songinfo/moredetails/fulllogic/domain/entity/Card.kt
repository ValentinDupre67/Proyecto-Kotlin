package ayds.songinfo.moredetails.fulllogic.domain.entity

import ayds.artist.external.lastfm.data.Source

data class Card(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: CardSource,
    var isLocallyStored: Boolean = false
)
enum class CardSource{
    LASTFM
}
