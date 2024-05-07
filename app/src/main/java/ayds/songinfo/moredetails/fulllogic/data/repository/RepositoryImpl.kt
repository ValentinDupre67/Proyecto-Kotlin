package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.external.ArtistAPIRequest
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

class RepositoryImpl : DetailsRepository {

    override lateinit var artistAPIRequest : ArtistAPIRequest
    override lateinit var articleDatabase: ArticleDatabase

    override fun getArticle(artistName: String): ArticleEntity {
        articleDatabase = DetailsRepositoryInjector.getArticleDatabase()
        val dbArticle = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return if (dbArticle != null) {
            dbArticle.markItAsLocal()
        } else {
            artistAPIRequest = DetailsRepositoryInjector.getArtistAPIRequest()
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
