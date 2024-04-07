package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import java.text.DateFormatSymbols

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong -> {
                val releaseDateParts = song.releaseDate.split("-")
                val year = releaseDateParts.first()
                val month = releaseDateParts.getOrNull(1)?.toIntOrNull()?.let { monthNumber ->
                    DateFormatSymbols().months[monthNumber - 1]
                }

                val releaseDateText = when (song.releaseDatePresicion) {
                    "year" -> {
                        "$year (not a leap year)"
                    }
                    "month" -> {
                        "$month, $year"
                    }
                    "day" -> {
                        "${releaseDateParts[2]}/${releaseDateParts[1]}/$year"
                    }
                    else -> song.releaseDate
                }

                "Song: ${song.songName} ${if (song.isLocallyStored) "[*]" else ""}\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: $releaseDateText"
            }
            else -> "Song not found"
        }
    }

}