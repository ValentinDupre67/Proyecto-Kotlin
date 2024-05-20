package ayds.artist.external.lastfm.data


enum class Source {
    LASTFM, NYTIMES, WIKIPEDIA
}
data class LastFMCard(
    val artistName: String,
    val biography: String,
    val articleUrl: String,
    val source: Source,
    var isLocallyStored: Boolean = false
)
