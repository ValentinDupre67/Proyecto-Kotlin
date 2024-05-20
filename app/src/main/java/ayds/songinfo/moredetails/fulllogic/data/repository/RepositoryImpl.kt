package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.artist.external.lastfm.RemoteDataSource
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card

internal class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource : RemoteDataSource
) : DetailsRepository {
    override fun getArtist(artistName: String): Card {
        val dbArticle = localDataSource.getArticleByArtistName(artistName)
        val card: Card
        if (dbArticle != null) {
            card = dbArticle.apply { markItAsLocal() }
        } else {
            val remoteDataCard = remoteDataSource.getArticleByArtistName(artistName)
            card = Card(remoteDataCard.artistName, remoteDataCard.biography, remoteDataCard.articleUrl, remoteDataCard.isLocallyStored)
            if (card.biography.isNotEmpty()) {
                localDataSource.insertArtist(card)
            }
        }
        return card
    }
    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }

}
