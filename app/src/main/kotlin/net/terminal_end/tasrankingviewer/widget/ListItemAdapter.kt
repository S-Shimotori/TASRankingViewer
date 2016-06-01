package net.terminal_end.tasrankingviewer.widget

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.terminal_end.tasrankingviewer.R

/**
 * Created by S-Shimotori on 6/1/16.
 */

class ListItemAdapter(context: Context, objects: List<String>): ArrayAdapter<String>(context, 0, objects) {
    val foo = objects
    private val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View?

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.list_item, parent, false)
        } else {
            view = convertView
        }
        Log.d("getView", foo.reduce{ r, s -> r+s })
        val item = getItem(position)
        val text1 = view!!.findViewById(R.id.TitleText) as TextView
        text1.text = "Title: $item"
        val text2 = view!!.findViewById(R.id.SubTitleText) as TextView
        text2.text = "サブタイトル: " + foo[position] //SubTitle: $item"

        return view
    }
}