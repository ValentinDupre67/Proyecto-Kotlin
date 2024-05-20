package ayds.songinfo.moredetails.fulllogic.domain.entity

data class Card(
    val artistName: String,
    val biography: String,
    val articleUrl: String,
    var isLocallyStored: Boolean = false
)