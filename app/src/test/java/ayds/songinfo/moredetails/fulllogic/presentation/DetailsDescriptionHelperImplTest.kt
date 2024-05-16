package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
class DetailsDescriptionHelperImplTest {

    private val detailsDescriptionHelper = DetailsDescriptionHelperImpl()

    @Test
    fun `get description with empty artist details`() {
        val emptyArtistDetails = ArtistDetails(
            "",
            "",
            "",
            isLocallyStored = false
        )

        val result = detailsDescriptionHelper.getDescription(emptyArtistDetails)

        assertEquals("<html><div width=400><font face=\"arial\"><b></b></font></div></html>", result)
    }

}