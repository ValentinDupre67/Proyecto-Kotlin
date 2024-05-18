package ayds.songinfo.moredetails.fulllogic.data.repository

import DetailsRepository
import ayds.songinfo.moredetails.fulllogic.data.repository.external.RemoteDataSource
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
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
    private val remoteDataSource : RemoteDataSource = mockk(relaxUnitFun = true)
    private val repository : DetailsRepository = RepositoryImpl(localDataSource, remoteDataSource)

    @Test
    fun `when localDataSource return a artisDetail and mark it as local`() {
        //¿Porque no puede ser? ----> val artisDetailsTest : ArtistDetails = mockk()
        val artisDetailsTest = ArtistDetails(
            "nameMockk",
            "biographyMockk",
            "http/mockk.com",
            isLocallyStored = false
        )
        every { localDataSource.getArticleByArtistName("nameMockk") } returns artisDetailsTest;

        val result = repository.getArtist("nameMockk")

        assertEquals(result,artisDetailsTest)
        assertTrue(artisDetailsTest.isLocallyStored)

    }

    @Test
    fun `when local DataSource return a null, search in remote data source and not insert artisDetails in local DataSource `() {
        val artisDetailsTest = ArtistDetails(
            "nameMockk",
            "",
            "http/mockk.com",
            isLocallyStored = false
        )
        every { localDataSource.getArticleByArtistName("nameMockk") } returns null;
        every {remoteDataSource.getArticleByArtistName("nameMockk")} returns artisDetailsTest;

        assertFalse(artisDetailsTest.biography.isNotEmpty())
        verify(exactly = 0) { localDataSource.insertArtist(artisDetailsTest) }

        val result = repository.getArtist("nameMockk")

        assertEquals(result,artisDetailsTest)
    }
    @Test
    fun `when localDataSource return a null, search in remote data source and insert artisDetails in localDataSource `() {

        val artisDetailsTest = ArtistDetails(
            "nameMockk",
            "this is a biography",
            "http/mockk.com",
            isLocallyStored = false
        );

        every { localDataSource.getArticleByArtistName("nameMockk") } returns null;
        every {remoteDataSource.getArticleByArtistName("nameMockk")} returns artisDetailsTest;

        assertTrue(artisDetailsTest.biography.isNotEmpty())
        //verify { localDataSource.insertArtist(any()) } ¿por que si lo pongo aca no pasa el test?

        val result = repository.getArtist("nameMockk")
        assertEquals(result,artisDetailsTest)
        verify { localDataSource.insertArtist(any()) }
    }


}
