package ayds.songinfo.moredetails.fulllogic.data.repository.broker

import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card


interface Broker {
    fun getInfoFromExternal(artistName: String): List<Card>
}
class BrokerImpl(
    private val lastFMService : LastFMService
) : Broker {

    override fun getInfoFromExternal(artistName: String): List<Card>  {
        val listCards = mutableListOf<Card>()

        val lastFMCard = ProxyLastFM(lastFMService).getInfoFromLastFM(artistName)
        val nyTimesCard = ProxyNYTimes().getInfoFromNYTimes(artistName)
        val wikipediaCard = ProxyWikipedia().getInfoFromWikipedia(artistName)

        if (lastFMCard != null) {
            listCards.add(lastFMCard)
        }
        if (nyTimesCard != null) {
            listCards.add(nyTimesCard)
        }
        if (wikipediaCard != null) {
            listCards.add(wikipediaCard)
        }

        return listCards
    }

}