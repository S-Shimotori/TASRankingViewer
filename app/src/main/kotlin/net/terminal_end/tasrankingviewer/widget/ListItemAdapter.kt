package net.terminal_end.tasrankingviewer.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.model.SearchQuery
import net.terminal_end.tasrankingviewer.model.VideoData
import okhttp3.*
import java.io.IOException
import java.util.*

/**
 * Created by S-Shimotori on 6/1/16.
 */

class ListItemAdapter(context: Context, objects: List<VideoData>): ArrayAdapter<VideoData>(context, 0, objects) {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View?

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_item, parent, false)
        } else {
            view = convertView
        }
        val item = getItem(position)

        val rankingTextView = view!!.findViewById(R.id.RankingTextView) as TextView
        rankingTextView.text = (position + 1).toString()

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
        thumbnailImageView.setImageDrawable(context.getDrawable(R.drawable.thumbnail))
        val okHttpHandler = OkHttpClient()
        val request = Request.Builder()
                .url(item.thumbnailUrl)
                .get()
                .build()
        okHttpHandler.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                // TODO
            }

            override fun onResponse(call: Call?, response: Response?) {
                val handler = Handler(Looper.getMainLooper())
                if (response != null && response.isSuccessful) {
                    val inputStream = response.body().byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    handler.post {
                        thumbnailImageView.setImageBitmap(bitmap)
                    }
                }
            }
        })

        val cmsIdTextView = view!!.findViewById(R.id.CmsIdTextView) as TextView
        cmsIdTextView.text = item.cmsId

        val tasPointTextView = view!!.findViewById(R.id.TasPointTextView) as TextView
        tasPointTextView.text = item.getScore().toString() + "pt"

        val titleTextView = view!!.findViewById(R.id.TitleTextView) as TextView
        titleTextView.text = item.title

        val tagsTextView = view!!.findViewById(R.id.TagsTextView) as TextView
        tagsTextView.text = item.tags.joinToString(" ")

        val viewCounterTextView = view!!.findViewById(R.id.ViewCounterTextView) as TextView
        viewCounterTextView.text = item.viewCounter.toString()

        val commentCounterTextView = view!!.findViewById(R.id.CommentCounterTextView) as TextView
        commentCounterTextView.text = item.commentCounter.toString()

        val myListCounterTextView = view!!.findViewById(R.id.MyListCounterTextView) as TextView
        myListCounterTextView.text = item.myListCounter.toString()

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
}