import ayds.songinfo.moredetails.fulllogic.data.repository.RepositoryImpl
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsView

interface DetailsPresenter {
    fun onViewCreated()
}
class DetailsPresenterImpl(private val view: DetailsView) : DetailsPresenter {

    override fun onViewCreated() {
        val repository = RepositoryImpl(view.getContext())
        val artistName = view.uiState.artistName
        view.updateUi(repository.getArticle(artistName))
    }
}