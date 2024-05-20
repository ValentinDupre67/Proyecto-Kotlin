package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsDescriptionHelperImplTest {

    private val detailsDescriptionHelper: DetailsDescriptionHelper = DetailsDescriptionHelperImpl()

    @Test
    fun `get description with empty artist details`() {
        val emptyArtistDetails = ArtistDetails(
            "",
            "",
            ""
        )

        val result = detailsDescriptionHelper.getDescription(emptyArtistDetails)

        val expected = "<html><div width=400><font face=\"arial\">" +
                            "<b></b>" +
                            "</font></div></html>"

        assertEquals(expected, result)
    }

     @Test
     fun `given a local artist it should return the description`() {
         val artistDetails = ArtistDetails(
             "Green Day",
             "biography",
             "url",
             isLocallyStored = true
         )

         val result = detailsDescriptionHelper.getDescription(artistDetails)

         val expected = "<html><div width=400><font face=\"arial\">" +
                            "[*]biography" +
                            "</font></div></html>"

         assertEquals(expected, result)
     }

    @Test
    fun `given a non local artist it should return the description`() {
        val artistDetails = ArtistDetails(
            "Green Day",
            "biography",
            "url",
            isLocallyStored = false
        )

        val result = detailsDescriptionHelper.getDescription(artistDetails)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "biography" +
                "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `biography should highlight artist name regardless of case`() {
        val artistDetails = ArtistDetails(
            "Green Day",
            "Green Day is a band. green day is popular. GREEN DAY rocks.",
            "url"
        )

        val result = detailsDescriptionHelper.getDescription(artistDetails)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "<b>GREEN DAY</b> is a band. <b>GREEN DAY</b> is popular. <b>GREEN DAY</b> rocks." +
                "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `biography should convert new lines to HTML breaks`() {
        val artistDetails = ArtistDetails(
            "Beatles",
            "The Beatles are a legendary band.\nThey changed the music world.",
            "url"
        )

        val result = detailsDescriptionHelper.getDescription(artistDetails)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "The <b>BEATLES</b> are a legendary band.<br>They changed the music world." +
                "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `biography should convert double slash to HTML breaks`() {
        val artistDetails = ArtistDetails(
            "Beatles",
            "The Beatles are a legendary band.\\nThey changed the music world.",
            "url"
        )

        val result = detailsDescriptionHelper.getDescription(artistDetails)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "The <b>BEATLES</b> are a legendary band.<br>They changed the music world." +
                "</font></div></html>"

        assertEquals(expected, result)
    }

}