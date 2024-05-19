package ayds.songinfo.moredetails.fulllogic.data.repository.local

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

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
        val artistDetailsTest = ArtistDetails(
            "nameMockk",
            "this's a biography",
            "url of nameMockk",
            isLocallyStored = false
        )
        every { articleDatabase.ArticleDao().getArticleByArtistName(artistName) }returns articleEntityTest;

        val result = localDataSource.getArticleByArtistName(artistName)

        assertNotNull(articleEntityTest)
        assertEquals(result,artistDetailsTest)
    }

    @Test
    fun `when  a method isertArtist, insert a valida articleEntity`(){
        val articleEntityTest = ArticleEntity(
            "pedroMockk",
            "this's a biography of pedroMockk",
            "url of pedroMockk"
        )
        every { articleDatabase.ArticleDao().insertArticle(articleEntityTest) } just Runs
        every { articleDatabase.ArticleDao().getArticleByArtistName("pedroMockk") } returns articleEntityTest

        localDataSource.insertArtist(ArtistDetails(
            artistName = "pedroMockk",
            biography = "this's a biography of pedroMockk",
            articleUrl = "url of pedroMockk"
        ))

        val result = localDataSource.getArticleByArtistName("pedroMockk")

        assertNotNull(result)
        assertEquals(articleEntityTest.artistName, result?.artistName)
        assertEquals(articleEntityTest.biography, result?.biography)
        assertEquals(articleEntityTest.articleUrl, result?.articleUrl)

    }
}