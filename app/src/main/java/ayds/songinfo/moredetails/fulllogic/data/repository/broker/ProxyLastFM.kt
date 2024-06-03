package ayds.songinfo.moredetails.fulllogic.data.repository.broker

import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

class ProxyLastFM(private val lastFMService: LastFMService) {
    fun getInfoFromLastFM(artistName: String): Card? {
        val lastFMCard = lastFMService.getCardByArtistName(artistName)
        return Card(
            lastFMCard.artistName,
            lastFMCard.description,
            lastFMCard.infoUrl,
            CardSource.LASTFM
        )
        /*
        * LastFM nunca va a ser nulo, ya que esta programado para que devuelva un articulo(card) vacio
        * tendriamos que devolver un nulo, no un card con sus atributos vacios
        */

    }
}