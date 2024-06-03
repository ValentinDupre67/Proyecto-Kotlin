package ayds.songinfo.moredetails.fulllogic.data.repository.broker

import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

class ProxyWikipedia {
    fun getInfoFromWikipedia(artistName: String) : Card? {
        val wikipediaCard = WikipediaInjector.wikipediaTrackService.getInfo(artistName)
        return wikipediaCard?.let {
            Card(
                artistName,
                it.description,
                it.wikipediaURL,
                CardSource.WIKIPEDIA
            )
        }
    }

}