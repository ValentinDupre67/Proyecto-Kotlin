import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.data.repository.RepositoryImpl
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsDescriptionHelper
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsUiState

interface DetailsPresenter {
    val detailsUiObservable: Observable<DetailsUiState>
    fun getArtistInfo(artistName: String)
}
internal class DetailsPresenterImpl(
    private val detailsDescriptionHelper: DetailsDescriptionHelper,
    private val repository: RepositoryImpl
) : DetailsPresenter {

    override val detailsUiObservable = Subject<DetailsUiState>()
    override fun getArtistInfo(artistName : String) {
        val uiState = repository.getArtist(artistName).toUiState()

        detailsUiObservable.notify(uiState)
    }

    private fun ArtistDetails.toUiState() = DetailsUiState( /* Esta parte no la entiendo bien. Consultar */
        artistName,
        detailsDescriptionHelper.getDescription(this),
        articleUrl
    )
}