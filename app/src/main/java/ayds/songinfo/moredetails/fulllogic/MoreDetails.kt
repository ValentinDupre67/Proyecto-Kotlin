package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
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

class MoreDetails : Activity() {
    private val imageUrl =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    private val baseUrl =
        "https://ws.audioscrobbler.com/2.0/"
    private var textPanel: TextView? = null
    private var articleDatabase: ArticleDatabase? = null
    private var openUrlButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPanel = findViewById(R.id.textPanel)
        openUrlButton = findViewById(R.id.openUrlButton)
        open(intent.getStringExtra("artistName"))
    }

    fun getArtistInfo(artistName: String?) {

        Log.e("API", "artistName $artistName")
        Thread {
            val article = articleDatabase!!.ArticleDao().getArticleByArtistName(artistName!!)
            var biographyText = ""
            if (article != null) {
                biographyText = "[*]" + article.biography
                val urlString = article.articleUrl
                setOnClickListenerOnButton(urlString)
            } else { // get from service
                biographyText = getArtistFromService( artistName, biographyText)
            }
            updateItemsView(biographyText)
        }.start()
    }

    private fun getArtistFromService(
        artistName: String,
        biographyText: String
    ): String {
        val lastFMAPI = getArtistRequest()

        var biographyText1 = biographyText
        try {
            val callResponse: Response<String> = lastFMAPI.getArtistInfo(artistName).execute()
            Log.e("API", "JSON " + callResponse.body())
            val response = Gson().fromJson(callResponse.body(), JsonObject::class.java)
            val artist = response["artist"].getAsJsonObject()
            val artistBio = artist["bio"].getAsJsonObject()
            val contentBio = artistBio["content"]
            val artistUrl = artist["url"]
            if (contentBio == null) {
                biographyText1 = "No Results"
            } else {
                biographyText1 = contentBio.asString.replace("\\n", "\n")
                val textInHtml = textToHtml(biographyText1, artistName)

                insertArticleInDatabase(artistName, textInHtml, artistUrl)
            }
            val urlString = artistUrl.asString
            setOnClickListenerOnButton(urlString)
        } catch (ioException: IOException) {
            Log.e("TAG", "Error: $ioException")
            ioException.printStackTrace()
        }
        return biographyText1
    }

    private fun getArtistRequest(): ArtistAPIRequest {
        val retrofit = getRetrofit()
        val artistDao = retrofit.create(ArtistAPIRequest::class.java)
        return artistDao
    }

    private fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit
    }

    private fun updateItemsView(biographyText: String) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
            textPanel!!.text = Html.fromHtml(biographyText)
        }
    }

    private fun insertArticleInDatabase(artistName: String, textInHtml: String, artistUrl: JsonElement) {
        Thread {
            articleDatabase!!.ArticleDao().insertArticle(
                ArticleEntity(
                    artistName, textInHtml, artistUrl.asString
                )
            )
        }.start()
    }

    private fun setOnClickListenerOnButton(urlString: String?) {
        openUrlButton?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
    }

    private fun open(artist: String?) {
        articleDatabase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        getArtistInfo(artist)
    }

    companion object { // TODO: Sospechas de que está mal crear el objeto
        fun textToHtml(text: String, term: String?): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("<html><div width=400>")
            stringBuilder.append("<font face=\"arial\">")
            // TODO: Esto creo que debería hacerlo otra función
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            stringBuilder.append(textWithBold)
            stringBuilder.append("</font></div></html>")
            return stringBuilder.toString()
        }
    }
}
