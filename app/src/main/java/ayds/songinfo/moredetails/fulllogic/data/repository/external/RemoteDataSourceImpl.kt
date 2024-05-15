package ayds.songinfo.moredetails.fulllogic.data.repository.external

import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

interface RemoteDataSource {
    fun getArticleByArtistName(artistName: String): ArtistDetails
}

internal class RemoteDataSourceImpl (private val artistAPIRequest: ArtistAPIRequest): RemoteDataSource{
    override fun getArticleByArtistName(artistName: String): ArtistDetails {
        var artistDetails = ArtistDetails(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistDetails = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return artistDetails
    }

    private fun getArtistBioFromExternalData(serviceData: String?, artistName: String): ArtistDetails {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)
        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistDetails(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        artistAPIRequest.getArtistInfo(artistName).execute()

}