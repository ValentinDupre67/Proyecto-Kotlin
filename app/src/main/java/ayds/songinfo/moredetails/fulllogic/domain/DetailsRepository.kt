import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails

interface DetailsRepository {
     var articleDatabase: ArticleDatabase

     fun getArtist(artistName: String): ArtistDetails
}