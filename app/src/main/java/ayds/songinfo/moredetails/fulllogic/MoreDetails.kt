package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.data.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.external.ArtistAPIRequest
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val ARTICLE_BD_NAME = "database-name-thename"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class MoreDetails : Activity() {
    private lateinit var textPanel: TextView
    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var openUrlButton: Button
    private lateinit var logoImageView: ImageView
    private lateinit var artistAPIRequest : ArtistAPIRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initViewProperties()
        initializeArticleDatabase()
        initArtistAPIRequest()
        getArtistInfoAsync()
    }

    private fun initViewProperties() {
        textPanel = findViewById(R.id.textPanel)
        logoImageView = findViewById(R.id.imageView1);
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun initializeArticleDatabase() {
        articleDatabase =
            databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }

    private fun initArtistAPIRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        artistAPIRequest = retrofit.create(ArtistAPIRequest::class.java)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val articleEntity = getArtistInfoFromRepository()
        updateUi(articleEntity)
    }

    private fun getArtistInfoFromRepository(): ArticleEntity {
        val artistName = getArtistName()

        val dbArticle = getArticleFromDB(artistName)

        val artistEntity: ArticleEntity

        if (dbArticle != null) {
            artistEntity = dbArticle.markItAsLocal()
        } else {
            artistEntity = getArticleFromService(artistName)
            if (artistEntity.biography.isNotEmpty()) {
                insertArtistIntoDB(artistEntity)
            }
        }
        return artistEntity
    }

    private fun ArticleEntity.markItAsLocal() = copy(biography = "[*]$biography")

    private fun getArticleFromDB(artistName: String): ArticleEntity? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArticleEntity(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }

    private fun getArticleFromService(artistName: String): ArticleEntity {

        var articleEntity = ArticleEntity(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            articleEntity = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return articleEntity
    }

    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArticleEntity {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArticleEntity(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        artistAPIRequest.getArtistInfo(artistName).execute()

    private fun insertArtistIntoDB(articleEntity: ArticleEntity) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                articleEntity.artistName, articleEntity.biography, articleEntity.articleUrl
            )
        )
    }

    private fun updateUi(articleEntity: ArticleEntity) {
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

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateArticleText(articleEntity: ArticleEntity) {
        val text = articleEntity.biography.replace("\\n", "\n")
        textPanel.text = Html.fromHtml(textToHtml(text, articleEntity.artistName))
    }

    private fun textToHtml(text: String, term: String?): String {
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

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
