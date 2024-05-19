package ayds.songinfo.moredetails.fulllogic.data.repository.local

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
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
}