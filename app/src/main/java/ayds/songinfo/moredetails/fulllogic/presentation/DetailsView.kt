package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.injector.DependencyInjector
import com.squareup.picasso.Picasso


class DetailsViewActivity : AppCompatActivity() {

    private lateinit var textPanel: TextView
    private lateinit var openUrlButton: Button
    private lateinit var logoImageView: ImageView

    private lateinit var detailsPresenter: DetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_view)

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
            updateOpenUrlButton(detailsUiState.articleUrl)
            updateLastFMLogo(detailsUiState.imageUrl)
            updateArticleText(detailsUiState.biography)
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