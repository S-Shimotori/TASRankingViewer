package net.terminal_end.tasrankingviewer.model

import android.text.Html
import com.github.kittinunf.result.Result
import net.terminal_end.tasrankingviewer.util.toCalendar
import java.util.*

/**
 * Created by S-Shimotori on 6/3/16.
 */

class VideoData(
        val cmsId: String, title: String, val tags: List<String>, val startTime: Calendar, val thumbnailUrl: String, val viewCounter: Int, val commentCounter: Int, val myListCounter: Int) {
    val title = Html.fromHtml(title)

    companion object {
        fun convertFromChunkHitsValues(values: List<Chunk.Value.Hits>): Result<List<VideoData>, Exception> {
            return Result.Success(values.map {
                if (it.cmsid != null || it.title != null || it.tags != null || it.start_time != null || it.thumbnail_url != null ||
                        it.view_counter != null || it.comment_counter != null || it.mylist_counter != null) {
                    val startTimeCalendar = it.start_time!!.toCalendar()
                    when (startTimeCalendar) {
                        is Result.Success -> {
                            VideoData(it.cmsid!!, it.title!!, it.tags!!.split(" "), startTimeCalendar.value, it.thumbnail_url!!, it.view_counter!!, it.comment_counter!!, it.mylist_counter!!)
                        }
                        is Result.Failure -> {
                            return Result.Failure(startTimeCalendar.error)
                        }
                    }
                } else {
                    return Result.Failure(NullPointerException())
                }
            })
        }
    }

    fun getScore(): Int {
        return viewCounter + commentCounter + myListCounter * 20
    }

}