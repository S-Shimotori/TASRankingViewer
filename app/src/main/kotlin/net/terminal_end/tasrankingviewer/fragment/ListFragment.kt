package net.terminal_end.tasrankingviewer.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.model.SearchQuery
import net.terminal_end.tasrankingviewer.model.SearchResponse
import net.terminal_end.tasrankingviewer.model.VideoData
import net.terminal_end.tasrankingviewer.widget.ListItemAdapter
import net.terminal_end.tasrankingviewer.widget.ListNoItemAdapter
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by S-Shimotori on 6/1/16.
 */

class ListFragment: Fragment() {
    companion object {
        fun newInstance(position: Int): ListFragment {
            val listFragment = ListFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            listFragment.arguments = bundle
            return listFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val listView = inflater!!.inflate(R.layout.list_view, null) as ListView
        listView.adapter = ListNoItemAdapter(context, listOf(resources.getString(R.string.loading)))
        listView.divider.alpha = 0

        when (arguments.getInt("position")) {
            0 -> {
                setListItem(listView, null, SearchQuery.SortBy.start_time, null, null)
            }
            1 -> {
                val calendarThisMonth = Calendar.getInstance()
                setRankingByMonth(listView, calendarThisMonth.get(Calendar.MONTH))
            }
            2 -> {
                val calendarLastMonth = Calendar.getInstance()
                calendarLastMonth.add(Calendar.MONTH, -1)
                setRankingByMonth(listView, calendarLastMonth.get(Calendar.MONTH))
            }
        }
        return listView
    }

    fun setRankingByMonth(listView: ListView, month: Int) {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val calendarBeginningOfMonth = Calendar.getInstance()
        calendarBeginningOfMonth.set(Calendar.MONTH, month)
        calendarBeginningOfMonth.set(Calendar.DATE, 1)
        calendarBeginningOfMonth.set(Calendar.HOUR, 0)
        calendarBeginningOfMonth.set(Calendar.MINUTE, 0)
        calendarBeginningOfMonth.set(Calendar.SECOND, 0)
        val calendarEndOfMonth = Calendar.getInstance()
        calendarEndOfMonth.set(Calendar.MONTH, month)
        calendarEndOfMonth.set(Calendar.DATE, calendarEndOfMonth.getActualMaximum(Calendar.DATE))
        calendarEndOfMonth.set(Calendar.HOUR_OF_DAY, 23)
        calendarEndOfMonth.set(Calendar.MINUTE, 59)
        calendarEndOfMonth.set(Calendar.SECOND, 59)
        val range = SearchQuery.Filter.Range.String(
                SearchQuery.Field.start_time,
                format.format(calendarBeginningOfMonth.time),
                format.format(calendarEndOfMonth.time),
                true, true
        )

        setListItem(listView, listOf(range), null, null) {
            it.getScore() * -1
        }
    }

    fun setListItem(listView: ListView, filters: List<SearchQuery.Filter>?, sortBy: SearchQuery.SortBy?, order: SearchQuery.Order?, sortByResult: ((VideoData) -> Int)?) {

        val query = SearchQuery.getInstance(
                "TAS OR TAP OR tool\"-\"",
                SearchQuery.SearchField.TAG, ListItemAdapter.getFieldList(), filters, sortBy, order, 0, 50
        )!!
        val jsonString = Gson().toJson(query)

        val client = OkHttpClient()
        val request = Request.Builder()
                .url(resources.getString(R.string.endpoint_search))
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString))
                .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    listView.adapter = ListNoItemAdapter(context, listOf(resources.getString(R.string.fail)))
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response != null && response.isSuccessful) {
                    val searchResponseString = response.body().string()
                    if (searchResponseString.contains("}\n{")) {
                        val hitsValues = SearchResponse.getInstance(searchResponseString).hits[0].values
                        if (hitsValues != null) {
                            val videoData = VideoData.convertFromChunkHitsValues(hitsValues)
                            when (videoData) {
                                is Result.Success -> {
                                    val handler = Handler(Looper.getMainLooper())
                                    handler.post {
                                        val objects = if (sortByResult != null) {
                                            videoData.value.sortedBy(sortByResult)
                                        } else {
                                            videoData.value
                                        }
                                        listView.adapter = ListItemAdapter(context, objects)
                                        listView.divider.alpha = 255
                                    }
                                    return
                                }
                            }
                        }
                    }
                }
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    listView.adapter = ListNoItemAdapter(context, listOf(resources.getString(R.string.fail)))
                }
            }
        })
    }

    class ListFragmentPagerAdapter(fm: FragmentManager, titles: List<String>): FragmentPagerAdapter(fm) {
        private val titles = titles

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
        override fun getItem(position: Int): android.support.v4.app.Fragment? {
            return ListFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return titles.size
        }
    }

}
