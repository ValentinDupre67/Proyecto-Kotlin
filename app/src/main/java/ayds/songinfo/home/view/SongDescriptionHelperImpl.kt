package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import java.text.DateFormatSymbols

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song): String
}

interface DateFormatter {
    fun format(date: String, precision: String): String
}

class DefaultDateFormatter : DateFormatter {
    override fun format(date: String, precision: String): String {
        return when (precision) {
            "year" -> "${date.substringBefore("-")} (not a leap year)"
            "month" -> {
                val (year, month) = date.split("-")
                val monthName = DateFormatSymbols().months[month.toInt() - 1]
                "$monthName, $year"
            }
            "day" -> {
                val (year, month, day) = date.split("-")
                "$day/$month/$year"
            }
            else -> date
        }
    }
}

class SpotifySongDescriptionHelper(
    private val dateFormatter: DateFormatter
) : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return if (song is SpotifySong) {
            val releaseDateText = dateFormatter.format(song.releaseDate, song.releaseDatePresicion)
            "Song: ${song.songName} ${if (song.isLocallyStored) "[*]" else ""}\n" +
                    "Artist: ${song.artistName}\n" +
                    "Album: ${song.albumName}\n" +
                    "Release date: $releaseDateText"
        } else {
            "Song not found"
        }
    }
}
