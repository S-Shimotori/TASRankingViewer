package net.terminal_end.tasrankingviewer.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.model.SearchQuery
import net.terminal_end.tasrankingviewer.model.SearchResponse
import net.terminal_end.tasrankingviewer.widget.ListItemAdapter
import okhttp3.*
import java.io.IOException

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
        val list = listOf(1..5).map { it.toString() }
        listView.adapter = ListItemAdapter(context, list)

        listView.setOnItemClickListener { adapterView, view, i, l ->
            val str = adapterView.getItemAtPosition(i) as String
            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
        }

        update()

        return listView
    }

    fun update() {
        val query = SearchQuery.getInstance(
                "TAS OR TAP OR tool\"-\"",
                SearchQuery.SearchField.TAG,
                listOf(SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.view_counter),
                listOf(SearchQuery.Filter.Range.String(
                        SearchQuery.Field.start_time,
                        "2016-05-01 00:00:00", "2016-05-31 23:59:59", true, true
                )),
                SearchQuery.SortBy.view_counter, SearchQuery.Order.desc, 0, 5
        )!!
        val jsonString = Gson().toJson(query)

        Log.d("ListFragment", jsonString)

        val client = OkHttpClient()
        val request = Request.Builder()
                .url(resources.getString(R.string.endpoint_search))
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString))
                .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("ListFragment", e.toString())
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response != null && response.isSuccessful) {
                    val searchResponseString = response.body().string()
                    val searchResponse = SearchResponse.getInstance(searchResponseString)
                    Log.d("ListFragment", searchResponse.hits[0].values!!.map { it.title }.joinToString(","))
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
