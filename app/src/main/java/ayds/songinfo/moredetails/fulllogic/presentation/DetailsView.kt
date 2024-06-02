package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.domain.entity.CardSource
import ayds.songinfo.moredetails.fulllogic.injector.DependencyInjector
import com.squareup.picasso.Picasso


class DetailsViewActivity : AppCompatActivity() {

    private lateinit var textPanel: TextView
    private lateinit var textSource: TextView
    private lateinit var openUrlButton: Button
    private lateinit var logoImageView: ImageView

    private lateinit var textDescriptionNY: TextView
    private lateinit var textSourceNY: TextView
    private lateinit var openUrlNYButton: Button
    private lateinit var logoNYImageView: ImageView

    private lateinit var textDescriptionWikipedia: TextView
    private lateinit var textSourceWikipedia: TextView
    private lateinit var openUrlWikipediaButton: Button
    private lateinit var logoWikipediaImageView: ImageView


    private lateinit var detailsPresenter: DetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initViewProperties()

        observerPresenter()
        getArtistInfoAsync()
    }

    private fun initModule() {
        DependencyInjector.init(this)
        detailsPresenter = DependencyInjector.getDetailsPresenter()
    }

    private fun observerPresenter() {
        detailsPresenter.detailsUiObservable.subscribe { detailsUiState ->
            updateUi(detailsUiState)
        }
    }

    private fun initViewProperties() {
        textPanel = findViewById(R.id.textPanel)
        textSource = findViewById(R.id.textSource)
        logoImageView = findViewById(R.id.imageView1);
        openUrlButton = findViewById(R.id.openUrlButton)
        textDescriptionNY = findViewById(R.id.textPane2)
        textSourceNY = findViewById(R.id.textSource2)
        openUrlNYButton = findViewById(R.id.openUrlButton2)
        logoNYImageView = findViewById(R.id.imageView2)
        textDescriptionWikipedia = findViewById(R.id.textPane3)
        textSourceWikipedia = findViewById(R.id.textSource3)
        openUrlWikipediaButton = findViewById(R.id.openUrlButton3)
        logoWikipediaImageView = findViewById(R.id.imageView3)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val artistName = getArtistName()
        detailsPresenter.getArtistInfo(artistName)
    }

    private fun updateUi(detailsUiState: List<DetailsUiState>) {
        runOnUiThread {
            detailsUiState.forEach {
                when(it.source) {
                    CardSource.LASTFM    -> updateUiLastFM(it)
                    CardSource.NYTIMES   -> updateUiNYTimes(it)
                    CardSource.WIKIPEDIA -> updateUiWikipedia(it)
                }
            }
        }
    }
    private fun updateUiLastFM(uiState: DetailsUiState) {
        updateSourceText(uiState.source, textSource)
        updateOpenUrlButton(uiState.infoUrl, openUrlButton)
        updateImageLogo(uiState.imageUrl.get(0), logoImageView)
        updateArticleText(uiState.description, textPanel)
    }

    private fun updateUiNYTimes(uiState: DetailsUiState) {
        updateSourceText(uiState.source, textSourceNY)
        updateOpenUrlButton(uiState.infoUrl, openUrlNYButton)
        updateImageLogo(uiState.imageUrl.get(1), logoNYImageView)
        updateArticleText(uiState.description, textDescriptionNY)
    }

    private fun updateUiWikipedia(uiState: DetailsUiState) {
        updateSourceText(uiState.source, textSourceWikipedia)
        updateOpenUrlButton(uiState.infoUrl, openUrlWikipediaButton)
        updateImageLogo(uiState.imageUrl.get(2), logoWikipediaImageView)
        updateArticleText(uiState.description, textSourceWikipedia)
    }

    private fun updateSourceText(source: CardSource, textView: TextView) {
        val prefix = "Source: "
        val sourceText = when (source) {
            CardSource.LASTFM -> "Last FM"
            CardSource.NYTIMES -> "New York Times"
            CardSource.WIKIPEDIA -> "Wikipedia"
        }
        textView.text = "$prefix$sourceText"
    }


    private fun updateOpenUrlButton(articleUrl: String, button: Button) {
        button.visibility = Button.VISIBLE
        button.setOnClickListener {
            navigateToUrl(articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateImageLogo(imageUrl: String, imageView: ImageView) {
        Picasso.get().load(imageUrl).into(imageView)
    }

    private fun updateArticleText(biography: String, textView: TextView) {
        textView.text = Html.fromHtml(biography)
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}