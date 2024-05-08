package ayds.songinfo.moredetails.fulllogic.data.repository.local

class LocalDataSource(private var articleDatabase: ArticleDatabase){
    fun getArticleByArtistName(artistName: String): ArticleEntity ?{
        val dbArticle = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return dbArticle
    }
}

