package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import DetailsPresenterImpl
import DetailsRepository
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
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
        val card = Card(
            artistName = artistName,
            description = "The Beatles were an English rock band.",
            infoUrl = articleUrl,
            isLocallyStored = true
        )
        val expectedUiState = DetailsUiState(
            artistName = artistName,
            description = biography,
            articleUrl = articleUrl
        )

        every { detailsDescriptionHelper.getDescription(card) } returns biography
        every { repository.getArtist(artistName) } returns card

        val uiStateTester: (DetailsUiState) -> Unit = mockk(relaxed = true)
        detailsPresenter.detailsUiObservable.subscribe {
            uiStateTester(it)
        }

        detailsPresenter.getArtistInfo(artistName)

        verify { uiStateTester(expectedUiState) }
    }

}