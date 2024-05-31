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
import ayds.artist.external.lastfm.data.Source
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.injector.DependencyInjector
import com.squareup.picasso.Picasso


class DetailsViewActivity : AppCompatActivity() {

    private lateinit var textPanel: TextView
    private lateinit var textSource: TextView
    private lateinit var openUrlButton: Button
    private lateinit var logoImageView: ImageView

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

    private fun updateUi(detailsUiState: DetailsUiState) {
        runOnUiThread {
            updateOpenUrlButton(detailsUiState.infoUrl)
            updateLastFMLogo(detailsUiState.imageUrl)
            updateArticleText(detailsUiState.description)
        }
    }

    private fun updateSourceText(source: Source) {
        val prefix = "Source: "
        textSource.text = when (source) {
            Source.LASTFM -> prefix + "Last FM"
            Source.NYTIMES -> prefix + "New York Times"
            Source.WIKIPEDIA -> prefix + "Wikipedia"
        }
    }

    private fun updateOpenUrlButton(articleUrl: String) {
        openUrlButton.setOnClickListener {
            navigateToUrl(articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateLastFMLogo(imageUrl: String) {
        Picasso.get().load(imageUrl).into(logoImageView)
    }

    private fun updateArticleText(biography: String) {
        textPanel.text = Html.fromHtml(biography)
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}