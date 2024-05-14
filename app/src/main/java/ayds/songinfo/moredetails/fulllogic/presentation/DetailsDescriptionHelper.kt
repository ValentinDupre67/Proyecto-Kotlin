package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity
import retrofit2.http.HEAD
import java.util.Locale

interface DetailsDescriptionHelper {
    fun getDescription(articleEntity: ArticleEntity): String

}

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"
internal class DetailsDescriptionHelperImpl: DetailsDescriptionHelper {
    override fun getDescription(articleEntity: ArticleEntity): String {
        val text = getTextBiography(articleEntity)
        return textToHtml(text, articleEntity.artistName)
    }

    private fun getTextBiography(articleEntity: ArticleEntity): String {
        //val prefix = if (artistBiography.isLocallyStored) "[*]" else ""
        // TODO: Para incluir esta linea deberiamos sacar una parte de la implementación de Repository
        return articleEntity.biography.replace("\\n", "\n")
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

