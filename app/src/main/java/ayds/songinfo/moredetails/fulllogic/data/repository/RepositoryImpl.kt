package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity

class RepositoryImpl : DetailsRepository {
    override lateinit var articleDatabase: ArticleDatabase
    override fun getArticle(artistName: String): ArticleEntity {
        val dbArticle = DetailsRepositoryInjector.getLocalDataSource().getArticleByArtistName(artistName)
        val articleEntity: ArticleEntity
        return if (dbArticle != null) {
            dbArticle.markItAsLocal()
        } else {
            articleEntity = DetailsRepositoryInjector.getRemoteDataSource().getArticleByArtistName(artistName)
            if (articleEntity.biography.isNotEmpty()) {
                insertArticleIntoDB(articleEntity)
            }
            articleEntity
        }
    }

    private fun ArticleEntity.markItAsLocal() = copy(biography = "[*]$biography")

    private fun insertArticleIntoDB(articleEntity: ArticleEntity) {
        articleDatabase.ArticleDao().insertArticle(articleEntity)
    }

}
