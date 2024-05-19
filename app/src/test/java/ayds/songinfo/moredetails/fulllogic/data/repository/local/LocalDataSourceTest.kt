package ayds.songinfo.moredetails.fulllogic.data.repository.local

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
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
}