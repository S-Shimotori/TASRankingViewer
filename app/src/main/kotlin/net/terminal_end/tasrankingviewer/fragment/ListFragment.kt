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
import android.widget.AbsListView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.model.SearchError
import net.terminal_end.tasrankingviewer.model.SearchQuery
import net.terminal_end.tasrankingviewer.model.SearchResponse
import net.terminal_end.tasrankingviewer.model.VideoData
import net.terminal_end.tasrankingviewer.widget.ListItemAdapter
import okhttp3.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.onError
import rx.schedulers.Schedulers
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by S-Shimotori on 6/1/16.
 */

class ListFragment: Fragment() {
    var maxItem: Int? = null
    var isLoading = false
    val query = "TAS OR TAP OR tool\"-\""
    val mediaType = "application/json; charset=utf-8"
    val requestNewArrivalSize = 40
    val handler = Handler(Looper.getMainLooper())
    var adapter: ListItemAdapter? = null
    lateinit var button: Button
    lateinit var linearLayout: LinearLayout

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
        linearLayout = inflater!!.inflate(R.layout.list_view, null) as LinearLayout
        val listView = linearLayout.findViewById(R.id.ListView) as ListView
        button = Button(context)
        button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        button.text = resources.getString(R.string.reload_button_text)

        when (arguments.getInt("position")) {
            0 -> {
                adapter = ListItemAdapter(context, ArrayList(), false)
                listView.adapter = adapter
                button.setOnClickListener {
                    loadNewArrival(listView, 0)
                }
                listView.setOnScrollListener(object: AbsListView.OnScrollListener {
                    override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
                        if (!isLoading && (maxItem == null || (p1 + p2 == p3) && p3 < maxItem!!)) {
                            isLoading = true
                            loadNewArrival(listView, p3)
                        }
                    }

                    override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
                    }
                })
            }
            1 -> {
                val calendarThisMonth = Calendar.getInstance()
                setRankingToList(listView, calendarThisMonth.get(Calendar.MONTH))
                button.setOnClickListener {
                    setRankingToList(listView, calendarThisMonth.get(Calendar.MONTH))
                }
            }
            2 -> {
                val calendarLastMonth = Calendar.getInstance()
                calendarLastMonth.add(Calendar.MONTH, -1)
                setRankingToList(listView, calendarLastMonth.get(Calendar.MONTH))
                button.setOnClickListener {
                    setRankingToList(listView, calendarLastMonth.get(Calendar.MONTH))
                }
            }
        }
        return linearLayout
    }

    fun createFilterForMonth(month: Int): SearchQuery.Filter {
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
        return SearchQuery.Filter.Range.String(
                SearchQuery.Field.start_time,
                format.format(calendarBeginningOfMonth.time),
                format.format(calendarEndOfMonth.time),
                true, true
        )
    }

    fun setRankingToList(listView: ListView, month: Int) {
        val filter = createFilterForMonth(month)
        val objects = mutableListOf<VideoData>()
        Observable.create<Int> {
            val searchQuery = SearchQuery.getInstance(query, SearchQuery.SearchField.TAG, listOf(SearchQuery.Field.cmsid), listOf(filter), null, null, 0, 0)
            val jsonString = Gson().toJson(searchQuery)
            val request = Request.Builder()
                    .url(resources.getString(R.string.endpoint_search))
                    .post(RequestBody.create(MediaType.parse(mediaType), jsonString))
                    .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    it.onError(e)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null && response.isSuccessful) {
                        val searchResponseString = response.body().string()
                        if (searchResponseString.contains("}\n{")) {
                            val stats = SearchResponse.getInstance(searchResponseString).stats
                            if (stats.size > 0 && stats[0].values != null) {
                                val statsValues = stats[0].values
                                if (statsValues!!.size > 0) {
                                    it.onNext(statsValues[0].total)
                                    it.onCompleted()
                                    return
                                }
                            }
                        } else {
                            val error = Gson().fromJson(searchResponseString, SearchError::class.java)
                            it.onError(SearchError.Exception(error.errid, ""))
                            response.close()
                            return
                        }
                    } else if (response != null) {
                        it.onError(SearchError.Exception(response.code().toString(), ""))
                        response.close()
                    } else {
                        it.onError(NullPointerException("response"))
                    }
                }
            })
        }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .onError {
            showReloadButton()
        }.flatMap {
            val parts = (0 .. (it/SearchQuery.MAX_SIZE)).toList().map {
                createPartialObservable(it * SearchQuery.MAX_SIZE, listOf(filter))
            }
            Observable.zip(parts) {
                it.fold(mutableListOf<VideoData>()) { result, list ->
                    result.addAll(list as List<VideoData>)
                    result
                }
            }
        }.subscribe({
            objects.addAll(it)
        }, {
            showReloadButton()
        }, {
            handler.post {
                listView.adapter = ListItemAdapter(context, objects.sortedBy { -1 * it.getScore() }, true)
                removeReloadButton()
            }
        })
    }

    fun createPartialObservable(from: Int, filters: List<SearchQuery.Filter>): Observable<List<VideoData>> {
        return Observable.create<List<VideoData>> {
            val searchQuery = SearchQuery.getInstance(query, SearchQuery.SearchField.TAG, ListItemAdapter.getFieldList(), filters, SearchQuery.SortBy.view_counter, null, from, SearchQuery.MAX_SIZE)
            val jsonString = Gson().toJson(searchQuery)
            val request = Request.Builder()
                    .url(resources.getString(R.string.endpoint_search))
                    .post(RequestBody.create(MediaType.parse(mediaType), jsonString))
                    .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    it.onError(e)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null && response.isSuccessful) {
                        val searchResponseString = response.body().string()
                        if (searchResponseString.contains("}\n{")) {
                            val hits = SearchResponse.getInstance(searchResponseString).hits
                            if (hits.size > 0 && hits[0].values != null) {
                                val videoData = VideoData.convertFromChunkHitsValues(hits[0].values!!)
                                when (videoData) {
                                    is Result.Success -> {
                                        it.onNext(videoData.value)
                                        it.onCompleted()
                                    }
                                    else -> {
                                        it.onError(SearchError.Exception("", "convert"))
                                        return
                                    }
                                }
                            } else {
                                it.onNext(listOf())
                                it.onCompleted()
                            }
                        } else {
                            val error = Gson().fromJson(searchResponseString, SearchError::class.java)
                            it.onError(SearchError.Exception(error.errid, ""))
                        }
                    } else if (response != null) {
                        it.onError(SearchError.Exception(response.code().toString(), ""))
                        response.close()
                        return
                    } else {
                        it.onError(SearchError.Exception("", "response"))
                    }
                }
            })
        }
    }

    fun loadNewArrival(listView: ListView, from: Int) {
        Observable.create<List<VideoData>> {
            val searchQuery = SearchQuery.getInstance(
                    query, SearchQuery.SearchField.TAG, ListItemAdapter.getFieldList(), null, SearchQuery.SortBy.start_time, null, from, requestNewArrivalSize
            )!!
            val jsonString = Gson().toJson(searchQuery)

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(resources.getString(R.string.endpoint_search))
                    .post(RequestBody.create(MediaType.parse(mediaType), jsonString))
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    it.onError(e)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null && response.isSuccessful) {
                        val searchResponseString = response.body().string()
                        if (searchResponseString.contains("}\n{")) {
                            val searchResponse = SearchResponse.getInstance(searchResponseString)
                            val hitsValues = searchResponse.hits[0].values
                            val total = searchResponse.stats[0].values?.get(0)?.total
                            if (hitsValues != null && total != null) {
                                val videoData = VideoData.convertFromChunkHitsValues(hitsValues)
                                when (videoData) {
                                    is Result.Success -> {
                                        maxItem = total
                                        it.onNext(videoData.value)
                                        it.onCompleted()
                                        return
                                    }
                                    is Result.Failure -> {
                                        it.onError(SearchError.Exception("", "convert"))
                                        return
                                    }
                                }
                            } else {
                                it.onNext(listOf())
                                it.onCompleted()
                                return
                            }
                        } else {
                            val error = Gson().fromJson(searchResponseString, SearchError::class.java)
                            it.onError(SearchError.Exception(error.errid.toString(), ""))
                            it.onCompleted()
                        }
                    } else if (response != null) {
                        it.onError(SearchError.Exception(response.code().toString(), ""))
                    } else {
                        it.onError(SearchError.Exception("", "response"))
                    }
                    response?.close()
                }
            })
        }.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe ({
            if (adapter == null) {
                adapter = ListItemAdapter(context, ArrayList(), false)
            }
            for (data in it) {
                adapter!!.add(data)
            }
        }, {
            handler.post {
                showReloadButton()
            }
        }, {
            handler.post {
                removeReloadButton()
                adapter!!.notifyDataSetChanged()
                listView.divider.alpha = 255

                val position = listView.firstVisiblePosition
                val yOffset = listView.getChildAt(0)?.top
                if (yOffset != null) {
                    listView.setSelectionFromTop(position, yOffset)
                }
                isLoading = false
            }
        })
    }

    fun showReloadButton() {
        handler.post {
            if (button.parent == null) {
                linearLayout.addView(button)
            }
        }
    }

    fun removeReloadButton() {
        handler.post {
            if (button.parent != null) {
                linearLayout.removeView(button)
            }
        }
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
