package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private  val ARTICLE_BD_NAME = "database-name-thename"
private val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class MoreDetails : Activity() {
    private var textPanel: TextView? = null
    private var articleDatabase: ArticleDatabase? = null
    private var openUrlButton: Button? = null
    private var logoImageView: ImageView? = null
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

    fun getArtistInfo() {
        val artistName = getArtistName()
        val article = articleDatabase!!.ArticleDao().getArticleByArtistName(artistName!!)
        var biographyText = ""
        if (article != null) {
            biographyText = "[*]" + article.biography
            val urlString = article.articleUrl
            setOnClickListenerButton(urlString)
        } else {
            biographyText = getArtistFromService( artistName, biographyText)
        }
        updateViewItems(biographyText)
    }

    private fun getArtistFromService(artistName: String, biographyText: String): String {
        var biographyTextAux = biographyText
        try {
            val callResponse: Response<String> =  artistAPIRequest.getArtistInfo(artistName).execute()
            Log.e("API", "JSON " + callResponse.body())
            val response = Gson().fromJson(callResponse.body(), JsonObject::class.java)
            val artist = response["artist"].getAsJsonObject()
            val artistBio = artist["bio"].getAsJsonObject()
            val contentBio = artistBio["content"]
            val artistUrl = artist["url"]
            contentBio?.let {
                biographyTextAux = processBiography(biographyText, artistName, artistUrl.asString)
            } ?: "No Results"
        } catch (ioException: IOException) {
            Log.e("TAG", "Error: $ioException")
            ioException.printStackTrace()
        }
        return biographyTextAux
    }

    private fun processBiography(biographyText: String, artistName: String, artistUrl: String): String {
        val formattedBiography = biographyText.replace("\\n", "\n")
        val textInHtml = textToHtml(formattedBiography, artistName)
        insertArticleInDatabase(artistName, textInHtml, artistUrl)
        setOnClickListenerButton(artistUrl)
        return formattedBiography
    }

    private fun updateViewItems(biographyText: String) {
        runOnUiThread {
            Picasso.get().load(LASTFM_IMAGE_URL).into(logoImageView)
            textPanel!!.text = Html.fromHtml(biographyText)
        }
    }

    private fun insertArticleInDatabase(artistName: String, textInHtml: String, artistUrl: String) {
        Thread {
            articleDatabase!!.ArticleDao().insertArticle(
                ArticleEntity(
                    artistName, textInHtml, artistUrl
                )
            )
        }.start()
    }

    private fun setOnClickListenerButton(urlString: String?) {
        openUrlButton?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
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

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
