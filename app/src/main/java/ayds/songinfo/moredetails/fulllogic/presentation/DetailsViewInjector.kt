package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import DetailsPresenterImpl
import ayds.songinfo.moredetails.fulllogic.data.repository.DetailsRepositoryInjector

object DetailsViewInjector {
    private lateinit var detailsPresenter: DetailsPresenter

    fun init(detailsView: DetailsView) {
        val detailsDescriptionHelper = DetailsDescriptionHelperImpl()

        detailsPresenter = DetailsPresenterImpl(detailsDescriptionHelper)
        DetailsRepositoryInjector.initRepository(detailsView)
    }

    fun getDetailsPresenter() : DetailsPresenter = detailsPresenter

}