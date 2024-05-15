package ayds.songinfo.moredetails.fulllogic.data.repository
import DetailsRepository
import ayds.songinfo.moredetails.fulllogic.data.repository.external.RemoteDataSource
import ayds.songinfo.moredetails.fulllogic.data.repository.local.LocalDataSource
import ayds.songinfo.moredetails.fulllogic.domain.entity.ArtistDetails

internal class RepositoryImpl(
    private val localDataSource: LocalDataSource, /* TODO: IMPORTANTE - HAY UN ERROR EN EL QUE ARTICLEDATABASE QUEDA COMO NO INICIALIZADO.*/
    private val remoteDataSource : RemoteDataSource
) : DetailsRepository { //TODO preguntar por internal class
    override fun getArtist(artistName: String): ArtistDetails {
        val dbArticle = localDataSource.getArticleByArtistName(artistName)
        val artistDetails: ArtistDetails
        if (dbArticle != null) {
            artistDetails = dbArticle.apply { markItAsLocal() }
        } else {
            artistDetails = DetailsRepositoryInjector.getRemoteDataSource().getArticleByArtistName(artistName)
            if (artistDetails.biography.isNotEmpty()) {
                localDataSource.insertArtist(artistDetails)
            }
        }
        return artistDetails
    }
    private fun ArtistDetails.markItAsLocal() {
        isLocallyStored = true
    }

}
