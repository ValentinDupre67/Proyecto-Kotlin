
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.domain.entity.Card
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsDescriptionHelper
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsUiState

interface DetailsPresenter {
    val detailsUiObservable: Observable<List<DetailsUiState>>
    fun getArtistInfo(artistName: String)
}
internal class DetailsPresenterImpl(
    private val detailsDescriptionHelper: DetailsDescriptionHelper,
    private val repository: DetailsRepository
) : DetailsPresenter {

    override val detailsUiObservable = Subject<List<DetailsUiState>>()
    override fun getArtistInfo(artistName : String) {
        val uiState = repository.getCard(artistName)
        val listDetailsUiState = mutableListOf<DetailsUiState>()
        uiState.forEach {
            listDetailsUiState.add(
                it.toUiState()
            )
        }

        detailsUiObservable.notify(listDetailsUiState)
    }

    private fun Card.toUiState() = DetailsUiState(
        artistName,
        detailsDescriptionHelper.getDescription(this),
        infoUrl,
        source
    )
}