package ayds.songinfo.moredetails.fulllogic.injector

import DetailsPresenter
import DetailsPresenterImpl
import android.content.Context
import androidx.room.Room
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.fulllogic.data.repository.RepositoryImpl
import ayds.songinfo.moredetails.fulllogic.data.repository.local.CardDatabase
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSourceImpl
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsDescriptionHelperImpl

private const val CARD_BD_NAME = "database-name-thename2"

object DependencyInjector {
    private lateinit var detailsPresenter: DetailsPresenter

    fun init(context: Context) {
        val detailsDescriptionHelper = DetailsDescriptionHelperImpl()

        val articleDatabase = Room.databaseBuilder(
            context,
            CardDatabase::class.java,
            CARD_BD_NAME
        ).build()
        val articleLocalStorage = LocalDataSourceImpl(articleDatabase)

        val repository = RepositoryImpl(articleLocalStorage, LastFMInjector.getRemoteData())

        detailsPresenter = DetailsPresenterImpl(detailsDescriptionHelper, repository)
    }

    fun getDetailsPresenter() : DetailsPresenter = detailsPresenter

}