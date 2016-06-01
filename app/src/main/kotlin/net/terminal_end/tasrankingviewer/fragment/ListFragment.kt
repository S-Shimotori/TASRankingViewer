package net.terminal_end.tasrankingviewer.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.widget.ListItemAdapter

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

        return listView
    }

    class ListFragmentPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        private val PAGE_COUNT = 5

        override fun getPageTitle(position: Int): CharSequence? {
            return position.toString()
        }
        override fun getItem(position: Int): android.support.v4.app.Fragment? {
            return ListFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return PAGE_COUNT
        }
    }

}
