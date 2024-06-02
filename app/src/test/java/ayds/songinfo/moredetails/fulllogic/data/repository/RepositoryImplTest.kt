package ayds.songinfo.moredetails.fulllogic.data.repository

import DetailsRepository
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class RepositoryImplTest{

    /*
    * private val localDataSource : LocalDataSource = mockk(relaxUnitFun) ¿Tendria que ponerlo asi?
    * */

    private val localDataSource : LocalDataSource = mockk(relaxUnitFun = true)
    private val remoteDataSource : LastFMService = mockk(relaxUnitFun = true)
    private val repository : DetailsRepository = RepositoryImpl(localDataSource, remoteDataSource)
    private val artistName = "nameMockk"

    @Test
    fun `when localDataSource return a artisDetail and mark it as local`() {
        //¿Porque no puede ser? ----> val artisDetailsTest : ArtistDetails = mockk()
        val cardTest = Card(
            "nameMockk",
            "biographyMockk",
            "http/mockk.com",
            isLocallyStored = false
        )
        every { localDataSource.getArticleByArtistName(artistName) } returns cardTest;

        val result = repository.getCard(artistName)

        assertEquals(result,cardTest)
        assertTrue(cardTest.isLocallyStored)
    }

    @Test
    fun `when local DataSource return a null, search in remote data source and not insert artisDetails in local DataSource `() {
        val artisDetailsTest = Card(
            "nameMockk",
            "",
            "http/mockk.com",
            isLocallyStored = false
        )
        every { localDataSource.getArticleByArtistName(artistName) } returns null;
        every {remoteDataSource.getArticleByArtistName(artistName)} returns artisDetailsTest;

        val result = repository.getCard(artistName)

        assertFalse(artisDetailsTest.description.isNotEmpty())
        assertEquals(result,artisDetailsTest)
        verify(exactly = 0) { localDataSource.insertArtist(artisDetailsTest) }
    }
    @Test
    fun `when localDataSource return a null, search in remote data source and insert artisDetails in localDataSource `() {

        val artisDetailsTest = Card(
            "nameMockk",
            "this is a biography",
            "http/mockk.com",
            isLocallyStored = false
        );

        every { localDataSource.getArticleByArtistName(artistName) } returns null;
        every {remoteDataSource.getArticleByArtistName(artistName)} returns artisDetailsTest;
        //verify { localDataSource.insertArtist(any()) } ¿por que si lo pongo aca no pasa el test??

        val result = repository.getCard(artistName)

        assertTrue(artisDetailsTest.description.isNotEmpty())
        assertEquals(result,artisDetailsTest)
        verify(exactly = 1) { localDataSource.insertArtist(any()) }
    }


}
