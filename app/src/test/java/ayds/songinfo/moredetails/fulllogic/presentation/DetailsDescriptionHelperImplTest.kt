package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsDescriptionHelperImplTest {

    private val detailsDescriptionHelper: DetailsDescriptionHelper = DetailsDescriptionHelperImpl()

    @Test
    fun `get description with empty artist details`() {
        val emptyCard = Card(
            "",
            "",
            ""
        )

        val result = detailsDescriptionHelper.getDescription(emptyCard)

        val expected = "<html><div width=400><font face=\"arial\">" +
                            "<b></b>" +
                            "</font></div></html>"

        assertEquals(expected, result)
    }

     @Test
     fun `given a local artist it should return the description`() {
         val card = Card(
             "Green Day",
             "biography",
             "url",
             isLocallyStored = true
         )

         val result = detailsDescriptionHelper.getDescription(card)

         val expected = "<html><div width=400><font face=\"arial\">" +
                            "[*]biography" +
                            "</font></div></html>"

         assertEquals(expected, result)
     }

    @Test
    fun `given a non local artist it should return the description`() {
        val card = Card(
            "Green Day",
            "biography",
            "url",
            isLocallyStored = false
        )

        val result = detailsDescriptionHelper.getDescription(card)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "biography" +
                "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `biography should highlight artist name regardless of case`() {
        val card = Card(
            "Green Day",
            "Green Day is a band. green day is popular. GREEN DAY rocks.",
            "url"
        )

        val result = detailsDescriptionHelper.getDescription(card)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "<b>GREEN DAY</b> is a band. <b>GREEN DAY</b> is popular. <b>GREEN DAY</b> rocks." +
                "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `biography should convert new lines to HTML breaks`() {
        val card = Card(
            "Beatles",
            "The Beatles are a legendary band.\nThey changed the music world.",
            "url"
        )

        val result = detailsDescriptionHelper.getDescription(card)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "The <b>BEATLES</b> are a legendary band.<br>They changed the music world." +
                "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `biography should convert double slash to HTML breaks`() {
        val card = Card(
            "Beatles",
            "The Beatles are a legendary band.\\nThey changed the music world.",
            "url"
        )

        val result = detailsDescriptionHelper.getDescription(card)

        val expected = "<html><div width=400><font face=\"arial\">" +
                "The <b>BEATLES</b> are a legendary band.<br>They changed the music world." +
                "</font></div></html>"

        assertEquals(expected, result)
    }

}