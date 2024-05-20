import ayds.songinfo.moredetails.fulllogic.domain.entity.Card

interface DetailsRepository {
     fun getArtist(artistName: String): Card
}