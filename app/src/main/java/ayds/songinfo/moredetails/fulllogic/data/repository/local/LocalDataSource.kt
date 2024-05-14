package ayds.songinfo.moredetails.fulllogic.data.repository.local

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails

class LocalDataSource(private var articleDatabase: ArticleDatabase){
    fun getArticleByArtistName(artistName: String): ArtistDetails?{
        val articleEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return articleEntity?.let {
            ArtistDetails(artistName, articleEntity.biography, articleEntity.articleUrl)
        }
    }
}

