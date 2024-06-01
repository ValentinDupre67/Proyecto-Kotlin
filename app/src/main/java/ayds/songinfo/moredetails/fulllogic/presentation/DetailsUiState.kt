package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

data class DetailsUiState(
    val artistName: String = "",
    val description: String = "",
    val infoUrl: String = "",
    val source: CardSource,
    val imageUrl: String =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
)
