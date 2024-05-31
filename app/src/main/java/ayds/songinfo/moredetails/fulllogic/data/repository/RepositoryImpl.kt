package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.artist.external.lastfm.data.RemoteDataSource
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource

internal class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource : RemoteDataSource
) : DetailsRepository {
    override fun getCard(artistName: String): Card {
        val dbArticle = localDataSource.getCardByArtistName(artistName)
        val card: Card
        if (dbArticle != null) {
            card = dbArticle.apply { markItAsLocal() }
        } else {
            val remoteDataCard = remoteDataSource.getCardByArtistName(artistName)
            card = Card(remoteDataCard.artistName, remoteDataCard.description, remoteDataCard.infoUrl,  CardSource.LASTFM)
            if (card.description.isNotEmpty()) {
                localDataSource.insertCard(card)
            }
        }
        return card
    }
    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }

}
