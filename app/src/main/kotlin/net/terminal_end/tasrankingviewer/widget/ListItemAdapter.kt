package net.terminal_end.tasrankingviewer.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.model.SearchQuery
import net.terminal_end.tasrankingviewer.model.VideoData
import java.util.*

/**
 * Created by S-Shimotori on 6/1/16.
 */

class ListItemAdapter(context: Context, objects: List<VideoData>, showRowId: Boolean): ArrayAdapter<VideoData>(context, 0, objects) {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val showRowId = showRowId

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View?

        val item = getItem(position)

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_item, parent, false)
        } else {
            view = convertView
        }

        val rankingTextView = view!!.findViewById(R.id.RankingTextView) as TextView
        if (showRowId) {
            rankingTextView.text = (position + 1).toString()
            rankingTextView.width = context.resources.getDimension(R.dimen.ranking_text_width).toInt()
        } else {
            rankingTextView.textSize = 0f
        }

        val dateTextView = view!!.findViewById(R.id.DateTextView) as TextView
        dateTextView.text =
                item.startTime.get(Calendar.YEAR).toString() +
                        context.getString(R.string.year) +
                        (item.startTime.get(Calendar.MONTH) + 1).toString() +
                        context.getString(R.string.month) +
                        item.startTime.get(Calendar.DATE) +
                        context.getString(R.string.date) +
                        item.startTime.get(Calendar.HOUR) +
                        context.getString(R.string.hour) +
                        item.startTime.get(Calendar.MINUTE) +
                        context.getString(R.string.minute) +
                        item.startTime.get(Calendar.SECOND) +
                        context.getString(R.string.second)

        val thumbnailImageView = view!!.findViewById(R.id.ThumbnailImageView) as ImageView
        thumbnailImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        thumbnailImageView.setImageDrawable(context.getDrawable(R.drawable.thumbnail))
        thumbnailImageView.setOnClickListener {
            openNicoVideo(context, item.cmsId)
        }
        Picasso.with(context).load(item.thumbnailUrl).into(thumbnailImageView)

        val cmsIdTextView = view!!.findViewById(R.id.CmsIdTextView) as TextView
        cmsIdTextView.text = item.cmsId

        val tasPointTextView = view!!.findViewById(R.id.TasPointTextView) as TextView
        tasPointTextView.text = item.getScore().toString() + "pt"

        val titleTextView = view!!.findViewById(R.id.TitleTextView) as TextView
        titleTextView.text = item.title
        titleTextView.setOnClickListener {
            openNicoVideo(context, item.cmsId)
        }

        val tagsTextView = view!!.findViewById(R.id.TagsTextView) as TextView
        tagsTextView.text = item.tags
                .filter { it != "TAS" && it != "TAP" && it != "ゲーム" }
                .joinToString("   ")

        val viewCounterTextView = view!!.findViewById(R.id.ViewCounterTextView) as TextView
        viewCounterTextView.text = "再生数: " + item.viewCounter.toString()

        val commentCounterTextView = view!!.findViewById(R.id.CommentCounterTextView) as TextView
        commentCounterTextView.text = "コメント数: " + item.commentCounter.toString()

        val myListCounterTextView = view!!.findViewById(R.id.MyListCounterTextView) as TextView
        myListCounterTextView.text = "マイリスト数: " + item.myListCounter.toString() + "  ×20"

        return view
    }

    companion object {
        fun getFieldList(): List<SearchQuery.Field> {
            return listOf(
                    SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.tags,
                    SearchQuery.Field.start_time, SearchQuery.Field.thumbnail_url,
                    SearchQuery.Field.view_counter, SearchQuery.Field.comment_counter, SearchQuery.Field.mylist_counter
            )
        }
    }

    fun openNicoVideo(context: Context, cmsId: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nicovideo.jp/watch/" + cmsId))
        context.startActivity(intent)
    }
}