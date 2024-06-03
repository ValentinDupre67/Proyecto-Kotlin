package ayds.songinfo.moredetails.fulllogic.data.repository.broker

import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

class ProxyNYTimes {
    fun getInfoFromNYTimes(artistName: String) : Card? {
        val nyTimesCard = NYTimesInjector.nyTimesService.getArtistInfo(artistName) as? NYTimesArticle.NYTimesArticleWithData
        return nyTimesCard?.let {
            Card(
                it.name.orEmpty(),
                it.info.orEmpty(),
                it.url,
                CardSource.NYTIMES
            )
        }
    }

}