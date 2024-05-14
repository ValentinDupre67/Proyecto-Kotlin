package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import java.util.Locale

interface DetailsDescriptionHelper {
    fun getDescription(artistDetails: ArtistDetails): String

}

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"
internal class DetailsDescriptionHelperImpl: DetailsDescriptionHelper {
    override fun getDescription(artistDetails: ArtistDetails): String {
        val text = getTextBiography(artistDetails)
        return textToHtml(text, artistDetails.artistName)
    }

    private fun getTextBiography(artistDetails: ArtistDetails): String {
        val prefix = if (artistDetails.isLocallyStored) "[*]" else ""
        val text = artistDetails.biography.replace("\\n", "\n")
        return "$prefix$text"
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append(HEADER)
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append(FOOTER)
        return builder.toString()
    }

}

