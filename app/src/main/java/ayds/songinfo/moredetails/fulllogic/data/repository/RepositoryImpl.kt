package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails

class RepositoryImpl : DetailsRepository {
    override lateinit var articleDatabase: ArticleDatabase /* TODO: IMPORTANTE - HAY UN ERROR EN EL QUE ARTICLEDATABASE QUEDA COMO NO INICIALIZADO.*/
    override fun getArticle(artistName: String): ArtistDetails {
        val dbArticle = DetailsRepositoryInjector.getLocalDataSource().getArticleByArtistName(artistName)
        val artistDetails: ArtistDetails
        if (dbArticle != null) {
            artistDetails = dbArticle.apply { markItAsLocal() }
        } else {
            artistDetails = DetailsRepositoryInjector.getRemoteDataSource().getArticleByArtistName(artistName)
            if (artistDetails.biography.isNotEmpty()) {
                insertArticleIntoDB(artistDetails)
            }
        }
        return artistDetails
    }

    private fun ArtistDetails.markItAsLocal() {
        isLocallyStored = true
    }

    private fun insertArticleIntoDB(artistDetails: ArtistDetails) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistDetails.artistName, artistDetails.biography, artistDetails.articleUrl
            )
        )
    }

}
