package ayds.songinfo.moredetails.fulllogic.data.repository.local

import ayds.songinfo.moredetails.fulllogic.domain.entity.Card


interface LocalDataSource{
    fun getArticleByArtistName(artisName: String): Card?
    fun insertArtist(card: Card)
}
internal class LocalDataSourceImpl(
    private val articleDatabase: ArticleDatabase):
    LocalDataSource {
    override fun getArticleByArtistName(artisName: String): Card?{
        val articleEntity = articleDatabase.ArticleDao().getArticleByArtistName(artisName)
        return articleEntity?.let {
            Card(artisName, articleEntity.biography, articleEntity.articleUrl)
        }
    }

    override fun insertArtist(card: Card) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                card.artistName, card.biography, card.articleUrl
            )
        )
    }
}

