package ayds.songinfo.moredetails.fulllogic.domain.entity

data class Card(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: CardSource,
    var isLocallyStored: Boolean = false
    //val LogoUrl = ""
)
enum class CardSource{
    LASTFM, NYTIMES, WIKIPEDIA
}
