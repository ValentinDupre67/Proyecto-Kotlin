import android.telecom.Call.Details
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsView

interface DetailsPresenter {
    fun onViewCreated()
}
class DetailsPresenterImpl(private val view: DetailsView) : DetailsPresenter{

    override fun onViewCreated() {
        val repository = RepositoryImpl()

        val artistName = view.uiState.artistName
        repository.getArticle(artistName) { articleEntity ->
            view.updateUi(articleEntity)
        }
    }
}