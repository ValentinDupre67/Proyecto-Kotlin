package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.artist.external.lastfm.data.LastFMService
import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

internal class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val lastFMService : LastFMService
) : DetailsRepository {
    override fun getCard(artistName: String): List<Card> {
        val dbArticleList = localDataSource.getCardByArtistName(artistName)
        val listCards = mutableListOf<Card>()


        if (dbArticleList[0] != null) {
            dbArticleList[0]?.apply { markItAsLocal() }?.let { listCards.add(it) }
        } else {
            val lastFMCard = fetchLastFmCard(artistName)
            listCards.add(lastFMCard)
            saveCardIfNotEmpty(lastFMCard)
        }

        if (dbArticleList[1] != null) {
            dbArticleList[1]?.apply { markItAsLocal() }?.let { listCards.add(it) }
        } else {
            val nyTimesCard = fetchNYTimesCard(artistName)
            if (nyTimesCard != null) {
                listCards.add(nyTimesCard)
                saveCardIfNotEmpty(nyTimesCard)
            }
        }

        if (dbArticleList[2] != null) {
            dbArticleList[2]?.apply { markItAsLocal() }?.let { listCards.add(it) }
        } else {
            val wikipediaCard = fetchWikipediaCard(artistName)
            if (wikipediaCard != null) {
                listCards.add(wikipediaCard)
                saveCardIfNotEmpty(wikipediaCard)
            }
        }


        return listCards

    }

    private fun fetchLastFmCard(artistName: String): Card {
        val remoteDataCard = lastFMService.getCardByArtistName(artistName)
        return Card(
            remoteDataCard.artistName,
            remoteDataCard.description,
            remoteDataCard.infoUrl,
            CardSource.LASTFM
        )
    }

    private fun fetchNYTimesCard(artistName: String): Card? {
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

    private fun fetchWikipediaCard(artistName: String): Card? {
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

    private fun saveCardIfNotEmpty(card: Card) {
        if (card.description.isNotEmpty()) {
            localDataSource.insertCard(card)
        }
    }

    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }

}
