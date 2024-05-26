package ayds.songinfo.moredetails.fulllogic.data.repository.local

import ayds.songinfo.moredetails.fulllogic.domain.entity.Card


interface LocalDataSource{
    fun getCardByArtistName(artisName: String): Card?
    fun insertCard(card: Card)
}
internal class LocalDataSourceImpl(
    private val cardDatabase: CardDatabase):
    LocalDataSource {
    override fun getCardByArtistName(artisName: String): Card?{
        val cardEntity = cardDatabase.CardDao().getCardByArtistName(artisName)
        return cardEntity?.let {
            Card(cardEntity.artistName, cardEntity.description, cardEntity.infoUrl, cardEntity.source, cardEntity.sourceLogoUrl)
        }
    }

    override fun insertCard(card: Card) {
        cardDatabase.CardDao().insertCard(
            CardEntity(
                card.artistName, card.description, card.infoUrl, card.source, card.sourceLogoUrl
            )
        )
    }
}

