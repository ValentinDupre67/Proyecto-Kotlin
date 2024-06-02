package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card

internal class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val lastFMService : LastFMService
) : DetailsRepository {
    override fun getCard(artistName: String): List<Card> {
        val dbArticleList = localDataSource.getCardByArtistName(artistName)
        val listCards = mutableListOf<Card>()


        if (dbArticleList[0] != null) {
            dbArticleList[0]?.apply { markItAsLocal() }?.let { listCards.add(it) }
        }

        if (dbArticleList[1] != null) {
            dbArticleList[1]?.apply { markItAsLocal() }?.let { listCards.add(it) }
        }

        if (dbArticleList[2] != null) {
            dbArticleList[2]?.apply { markItAsLocal() }?.let { listCards.add(it) }
        }

        val broker = BrokerImpl(lastFMService)
        listCards.addAll(broker.getInfoFromExternal(artistName))
        listCards.forEach { saveCardIfNotEmpty(it) }

        return listCards

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
