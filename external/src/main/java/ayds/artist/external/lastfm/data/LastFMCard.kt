package ayds.artist.external.lastfm.data


enum class Source {
    LASTFM, NYTIMES, WIKIPEDIA
}
data class LastFMCard(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: Source,
    val sourceLogoUrl: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png",
    var isLocallyStored: Boolean = false
)
