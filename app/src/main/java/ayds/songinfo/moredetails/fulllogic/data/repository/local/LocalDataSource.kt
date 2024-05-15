package ayds.songinfo.moredetails.fulllogic.data.repository.local

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails


interface LocalDataSource{
    fun getArticleByArtistName(artisName: String): ArtistDetails?
    fun insertArtist(artistDetails: ArtistDetails)
}
internal class LocalDataSourceImpl(
    private val articleDatabase: ArticleDatabase):
    LocalDataSource {
    override fun getArticleByArtistName(artisName: String): ArtistDetails?{
        val articleEntity = articleDatabase.ArticleDao().getArticleByArtistName(artisName)
        return articleEntity?.let {
            ArtistDetails(artisName, articleEntity.biography, articleEntity.articleUrl)
        }
    }

    override fun insertArtist(artistDetails: ArtistDetails) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistDetails.artistName, artistDetails.biography, artistDetails.articleUrl
            )
        )
    }
}

