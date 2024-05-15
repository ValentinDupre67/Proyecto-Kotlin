package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import DetailsPresenterImpl
import android.content.Context
import ayds.songinfo.moredetails.fulllogic.data.repository.DetailsRepositoryInjector


object DetailsViewInjector {
    private lateinit var detailsPresenter: DetailsPresenter

    fun init(context: Context) {
        val detailsDescriptionHelper = DetailsDescriptionHelperImpl()

        detailsPresenter = DetailsPresenterImpl(detailsDescriptionHelper)
        DetailsRepositoryInjector.initRepository(context)
    }

    fun getDetailsPresenter() : DetailsPresenter = detailsPresenter

}