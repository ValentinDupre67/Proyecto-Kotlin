package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.artist.external.lastfm.data.RemoteDataSource
import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

internal class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource : RemoteDataSource
) : DetailsRepository {
    override fun getCard(artistName: String): List<Card> {
        val dbArticle = localDataSource.getCardByArtistName(artistName)
        val listCards = mutableListOf<Card>()
        if (dbArticle != null) {
            listCards.add(dbArticle.apply { markItAsLocal() })
        } else {
            val lastFMCard = fetchLastFmCard(artistName)
            val nyTimesCard = fetchNYTimesCard(artistName)
            val wikipediaCard = fetchWikipediaCard(artistName)

            if (nyTimesCard != null) {
                listCards.add(nyTimesCard)
                saveCardIfNotEmpty(nyTimesCard)
            }

            if (wikipediaCard != null) {
                listCards.add(wikipediaCard)
                saveCardIfNotEmpty(wikipediaCard)
            }

            listCards.add(lastFMCard)
            saveCardIfNotEmpty(lastFMCard)
        }
        return listCards
    }

    private fun fetchLastFmCard(artistName: String): Card {
        val remoteDataCard = remoteDataSource.getCardByArtistName(artistName)
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
