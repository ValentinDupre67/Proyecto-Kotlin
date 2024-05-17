package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import DetailsPresenterImpl
import DetailsRepository
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class DetailsPresenterImplTest {

    private val detailsDescriptionHelper: DetailsDescriptionHelper = mockk()
    private val repository: DetailsRepository = mockk()

    private val detailsPresenter: DetailsPresenter = DetailsPresenterImpl(detailsDescriptionHelper, repository)

    @Test
    fun `on get artist info it should notify the result`() {
        val artistName = "The Beatles"
        val articleUrl = "http://thebeatles.com"
        val biography = "Description for The Beatles"
        val artistDetails = ArtistDetails(
            artistName = artistName,
            biography = "The Beatles were an English rock band.",
            articleUrl = articleUrl,
            isLocallyStored = true
        )
        val expectedUiState = DetailsUiState(
            artistName = artistName,
            biography = biography,
            articleUrl = articleUrl
        )

        every { detailsDescriptionHelper.getDescription(artistDetails) } returns biography
        every { repository.getArtist(artistName) } returns artistDetails

        val uiStateTester: (DetailsUiState) -> Unit = mockk(relaxed = true)
        detailsPresenter.detailsUiObservable.subscribe {
            uiStateTester(it)
        }

        detailsPresenter.getArtistInfo(artistName)

        verify { uiStateTester(expectedUiState) }
    }

}