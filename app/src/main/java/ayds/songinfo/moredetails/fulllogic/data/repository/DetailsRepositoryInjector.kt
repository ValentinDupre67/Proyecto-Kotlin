package ayds.songinfo.moredetails.fulllogic.data.repository

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.repository.external.ArtistAPIRequest
import ayds.songinfo.moredetails.fulllogic.data.repository.external.RemoteDataSource
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val ARTICLE_BD_NAME = "database-name-thename"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object DetailsRepositoryInjector {

    private lateinit var artistAPIRequest : ArtistAPIRequest
    private lateinit var articleDatabase: ArticleDatabase

    fun initRepository(detailsView: DetailsView) {
        initializeArticleDatabase(detailsView)
        initArtistAPIRequest()
    }

    private fun initializeArticleDatabase(detailsView: DetailsView) {
        articleDatabase =
            Room.databaseBuilder(detailsView as Context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }

    private fun initArtistAPIRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        artistAPIRequest = retrofit.create(ArtistAPIRequest::class.java)
    }

    fun getArtistAPIRequest(): ArtistAPIRequest = artistAPIRequest

    fun getArticleDatabase(): ArticleDatabase = articleDatabase

    fun getRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource(artistAPIRequest)
    }

    fun getLocalDataSource(): LocalDataSource {
        return LocalDataSource(getArticleDatabase())
    }
}