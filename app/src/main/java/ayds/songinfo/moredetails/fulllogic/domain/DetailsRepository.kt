import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails

interface DetailsRepository {
     fun getArtist(artistName: String): ArtistDetails
}