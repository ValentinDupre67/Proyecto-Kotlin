import ayds.songinfo.moredetails.fulllogic.domain.entity.Card

interface DetailsRepository {
     fun getCard(artistName: String): List<Card>
}