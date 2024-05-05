package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsPresenter
import DetailsRepository
import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.external.ArtistAPIRequest
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

private const val ARTICLE_BD_NAME = "database-name-thename"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

class RepositoryImpl(private val context: Context): DetailsRepository {

    override lateinit var artistAPIRequest : ArtistAPIRequest
    override lateinit var articleDatabase: ArticleDatabase
    private fun initializeArticleDatabase() {
        articleDatabase =
            databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }

    private fun initArtistAPIRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        artistAPIRequest = retrofit.create(ArtistAPIRequest::class.java)
    }

    override fun getArticle(artistName: String): ArticleEntity {
        initializeArticleDatabase()
        val dbArticle = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return if (dbArticle != null) {
            dbArticle.markItAsLocal()
        } else {
            initArtistAPIRequest()
            val articleEntity = getArticleFromService(artistName)
            if (articleEntity.biography.isNotEmpty()) {
                insertArticleIntoDB(articleEntity)
            }
            articleEntity
        }
    }

    private fun ArticleEntity.markItAsLocal() = copy(biography = "[*]$biography")

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

    private fun getArtistBioFromExternalData(serviceData: String?, artistName: String): ArticleEntity {
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

    private fun insertArticleIntoDB(articleEntity: ArticleEntity) {
        articleDatabase.ArticleDao().insertArticle(articleEntity)
    }

}
