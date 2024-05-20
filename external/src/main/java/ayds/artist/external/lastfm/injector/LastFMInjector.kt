package ayds.artist.external.lastfm.injector

import ayds.artist.external.lastfm.data.ArtistAPIRequest
import ayds.artist.external.lastfm.data.RemoteDataSource
import ayds.artist.external.lastfm.data.RemoteDataSourceImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
object LastFMInjector {

    fun getRemoteData() : RemoteDataSource {

        val artistAPIRequest = artistAPIRequest()

        return RemoteDataSourceImpl(artistAPIRequest)
    }

    private fun artistAPIRequest(): ArtistAPIRequest {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return  retrofit.create(ArtistAPIRequest::class.java)
    }

}