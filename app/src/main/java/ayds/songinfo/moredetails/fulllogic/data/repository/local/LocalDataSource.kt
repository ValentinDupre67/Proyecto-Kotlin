package ayds.songinfo.moredetails.fulllogic.data.repository.local

import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource


interface LocalDataSource{
    fun getCardByArtistName(artisName: String): List<Card?>
    fun insertCard(card: Card)
}
internal class LocalDataSourceImpl(
    private val cardDatabase: CardDatabase):
    LocalDataSource {
    override fun getCardByArtistName(artisName: String): List<Card?>{
        val listCards = mutableListOf<Card?>()
        val cardEntityLastFm = cardDatabase.CardDao().getCardByArtistNameAndSource(artisName,CardSource.LASTFM.ordinal)
        cardEntityLastFm?.let {
            val card = Card(cardEntityLastFm.artistName, cardEntityLastFm.description, cardEntityLastFm.infoUrl, CardSource.entries[cardEntityLastFm.source])
            listCards.add(card)
        }?: run {
            listCards.add(null)
        }

        val cardEntityNYTimes = cardDatabase.CardDao().getCardByArtistNameAndSource(artisName,CardSource.NYTIMES.ordinal)
        cardEntityNYTimes?.let {
            val card = Card(cardEntityNYTimes.artistName, cardEntityNYTimes.description, cardEntityNYTimes.infoUrl, CardSource.entries[cardEntityNYTimes.source])
            listCards.add(card)
        }?: run {
            listCards.add(null)
        }

        val cardEntityWikipedia = cardDatabase.CardDao().getCardByArtistNameAndSource(artisName,CardSource.WIKIPEDIA.ordinal)
        cardEntityWikipedia?.let {
            val card = Card(cardEntityWikipedia.artistName, cardEntityWikipedia.description, cardEntityWikipedia.infoUrl, CardSource.entries[cardEntityWikipedia.source])
            listCards.add(card)
        }?: run {
            listCards.add(null)
        }

        return listCards
    }

    override fun insertCard(card: Card) {
        cardDatabase.CardDao().insertCard(
            CardEntity(
                card.artistName, card.description, card.infoUrl, card.source.ordinal
            )
        )
    }
}

