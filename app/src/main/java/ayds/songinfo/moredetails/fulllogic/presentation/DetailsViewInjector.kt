package ayds.songinfo.moredetails.fulllogic.presentation

import DetailsPresenter
import DetailsPresenterImpl
import ayds.songinfo.moredetails.fulllogic.data.repository.DetailsRepositoryInjector

object DetailsViewInjector {
    private lateinit var detailsPresenter: DetailsPresenter

    fun init(detailsView: DetailsView) {
        detailsPresenter = DetailsPresenterImpl(detailsView)
        DetailsRepositoryInjector.initRepository(detailsView)
    }

    fun getDetailsPresenter() : DetailsPresenter = detailsPresenter

}