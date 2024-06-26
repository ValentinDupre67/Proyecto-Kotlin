package ayds.songinfo.moredetails.fulllogic.data.repository.local

class LocalDataSourceTest{

    private val articleDatabase : ArticleDatabase = mockk(relaxUnitFun = true)
    private val localDataSource : LocalDataSource = LocalDataSourceImpl(articleDatabase)
    private val artistName = "nameMockk"
    @Test
    fun `when the articleEntity is null, getArticleByArtistName should return null`(){
        every { articleDatabase.ArticleDao().getArticleByArtistName(artistName)} returns null;

        val result = localDataSource.getArticleByArtistName(artistName)

        assertEquals(result,null)
    }

    @Test
    fun `when the articleEntity in not null, getArticleByArtistName should return a valid ArtistDetails`(){
        val articleEntityTest = ArticleEntity(
            "nameMockk",
            "this's a biography",
            "url of nameMockk"
        )
        val cardTest = Card(
            "nameMockk",
            "this's a biography",
            "url of nameMockk",
            isLocallyStored = false
        )
        every { articleDatabase.ArticleDao().getArticleByArtistName(artistName) }returns articleEntityTest;

        val result = localDataSource.getArticleByArtistName(artistName)

        assertNotNull(articleEntityTest)
        assertEquals(result,cardTest)
    }

    @Test
    fun `when a method isertArtist, insert a valid ArtistDetails and the method getArticleByArtistName return a ArtistDetails previously inserted by isertArtist`(){
        val articleEntityTest = ArticleEntity(
            "pedroMockk",
            "this's a biography of pedroMockk",
            "url of pedroMockk"
        )
        every { articleDatabase.ArticleDao().insertArticle(articleEntityTest) } just Runs
        every { articleDatabase.ArticleDao().getArticleByArtistName("pedroMockk") } returns articleEntityTest

        localDataSource.insertArtist(Card(
            artistName = "pedroMockk",
            description = "this's a biography of pedroMockk",
            infoUrl = "url of pedroMockk"
        ))

        val result = localDataSource.getArticleByArtistName("pedroMockk")

        assertNotNull(result)
        assertEquals(articleEntityTest.artistName, result?.artistName)
        assertEquals(articleEntityTest.biography, result?.description)
        assertEquals(articleEntityTest.articleUrl, result?.infoUrl)

    }
}