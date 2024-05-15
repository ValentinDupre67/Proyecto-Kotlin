package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import DetailsPresenterImpl
import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.repository.DetailsRepositoryInjector
import ayds.songinfo.moredetails.fulllogic.data.repository.RepositoryImpl
import ayds.songinfo.moredetails.fulllogic.data.repository.external.ArtistAPIRequest
import ayds.songinfo.moredetails.fulllogic.data.repository.external.RemoteDataSource
import ayds.songinfo.moredetails.fulllogic.data.repository.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSourceImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val ARTICLE_BD_NAME = "database-name-thename"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object DetailsViewInjector {
    private lateinit var detailsPresenter: DetailsPresenter

    fun init(context: Context) {
        val detailsDescriptionHelper = DetailsDescriptionHelperImpl()

        val articleDatabase = Room.databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
        val articleLocalStorage = LocalDataSourceImpl(articleDatabase)

        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val artistAPIRequest = retrofit.create(ArtistAPIRequest::class.java)

        val remoteDataSource = RemoteDataSource(artistAPIRequest)

        val repository = RepositoryImpl(articleLocalStorage, remoteDataSource)



        detailsPresenter = DetailsPresenterImpl(detailsDescriptionHelper,repository)
        DetailsRepositoryInjector.initRepository(context)
    }

    fun getDetailsPresenter() : DetailsPresenter = detailsPresenter

}