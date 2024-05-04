import android.telecom.Call.Details
import ayds.songinfo.moredetails.fulllogic.presentation.DetailsView

interface DetailsPresenter {
    fun onViewCreated()
}
class DetailsPresenterImpl(private val view: DetailsView) : DetailsPresenter{
    override fun onViewCreated() {
        TODO("Not yet implemented")
    }

}