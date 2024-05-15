package ayds.songinfo.moredetails.fulllogic.data.repository

/*
private const val ARTICLE_BD_NAME = "database-name-thename"
internal const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
*/

object DetailsRepositoryInjector {
/*
    private lateinit var artistAPIRequest : ArtistAPIRequest
    private lateinit var articleDatabase: ArticleDatabase

    fun initRepository(context: Context) {
        initializeArticleDatabase(context)
        initArtistAPIRequest()
    }

    private fun initializeArticleDatabase(context: Context) {
        articleDatabase =
            Room.databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }

    private fun initArtistAPIRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        artistAPIRequest = retrofit.create(ArtistAPIRequest::class.java)
    }

    fun getArticleDatabase(): ArticleDatabase = articleDatabase

    fun getRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource(artistAPIRequest)
    }
    */
}