import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.external.ArtistAPIRequest
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails

interface DetailsRepository {
     var articleDatabase: ArticleDatabase

     fun getArticle(artistName: String): ArtistDetails
}