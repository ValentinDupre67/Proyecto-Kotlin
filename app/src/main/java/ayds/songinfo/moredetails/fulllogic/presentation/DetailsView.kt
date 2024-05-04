package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import DetailsPresenterImpl
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity
import com.squareup.picasso.Picasso
import java.util.*

interface DetailsView {
    val uiState: DetailsUiState
    fun updateUi(articleEntity: ArticleEntity)
}

private const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
class DetailsViewActivity : AppCompatActivity(), DetailsView{

    private lateinit var textPanel: TextView
    private lateinit var openUrlButton: Button
    private lateinit var logoImageView: ImageView
    private lateinit var detailsPresenter: DetailsPresenter
    override var uiState: DetailsUiState = DetailsUiState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_view)

        initViewProperties()
        initPresenter()
        getArtistInfoAsync()
    }

    private fun initPresenter() {
        detailsPresenter = DetailsPresenterImpl(this)
    }

    private fun initViewProperties() {
        textPanel = findViewById(R.id.textPanel)
        logoImageView = findViewById(R.id.imageView1);
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun getArtistInfoAsync() {
        Thread {
            detailsPresenter.onViewCreated()
        }.start()
    }

    override fun updateUi(articleEntity: ArticleEntity) {
        runOnUiThread {
            updateOpenUrlButton(articleEntity)
            updateLastFMLogo()
            updateArticleText(articleEntity)
        }
    }

    private fun updateOpenUrlButton(articleEntity: ArticleEntity) {
        openUrlButton.setOnClickListener {
            navigateToUrl(articleEntity.articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateLastFMLogo() {
        Picasso.get().load(LASTFM_IMAGE_URL).into(logoImageView)
    }

    private fun updateArticleText(articleEntity: ArticleEntity) {
        val text = articleEntity.biography.replace("\\n", "\n")
        textPanel.text = Html.fromHtml(textToHtml(text, articleEntity.artistName))
    }

    private fun textToHtml(text: String, term: String?): String { // TODO: Esto no estoy seguro que vaya en la view.
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

}