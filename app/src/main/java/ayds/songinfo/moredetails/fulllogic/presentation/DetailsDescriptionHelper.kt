package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import java.util.Locale

interface DetailsDescriptionHelper {
    fun getDescription(card: Card): String

}

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"
internal class DetailsDescriptionHelperImpl: DetailsDescriptionHelper {
    override fun getDescription(card: Card): String {
        val text = getTextBiography(card)
        return textToHtml(text, card.artistName)
    }

    private fun getTextBiography(card: Card): String {
        val prefix = if (card.isLocallyStored) "[*]" else ""
        val text = card.biography.replace("\\n", "\n") //TODO smell, no es bueno tener la lógica repartida
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

