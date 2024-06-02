package ayds.artist.external.lastfm.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

interface LastFMService {
    fun getCardByArtistName(artistName: String): LastFMCard
}

internal class LastFMServiceImpl (private val artistAPIRequest: LastFMAPI):
    LastFMService {
    override fun getCardByArtistName(artistName: String): LastFMCard {
        var artistDetails = LastFMCard(artistName, "", "", )
        try {
            val callResponse = getSongFromService(artistName)
            artistDetails = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return artistDetails
    }

    private fun getArtistBioFromExternalData(serviceData: String?, artistName: String): LastFMCard {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)
        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return LastFMCard(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        artistAPIRequest.getArtistInfo(artistName).execute()

}