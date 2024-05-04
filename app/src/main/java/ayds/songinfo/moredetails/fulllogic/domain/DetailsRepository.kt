import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.external.ArtistAPIRequest
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity

interface DetailsRepository {
     var artistAPIRequest : ArtistAPIRequest
     var articleDatabase: ArticleDatabase

     fun initRepository()
     fun getArticle(artistName: String): ArticleEntity
}