package ayds.songinfo.moredetails.fulllogic.data.repository.proxies

import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

class ProxyLastFM(private val lastFMService: LastFMService) {
    fun getInfoFromLastFM(artistName: String): Card? {
        val remoteDataCard = lastFMService.getCardByArtistName(artistName)
        return Card(
            remoteDataCard.artistName,
            remoteDataCard.description,
            remoteDataCard.infoUrl,
            CardSource.LASTFM
        )
    }
}