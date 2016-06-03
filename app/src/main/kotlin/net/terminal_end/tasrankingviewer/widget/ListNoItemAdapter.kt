package net.terminal_end.tasrankingviewer.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.terminal_end.tasrankingviewer.R

/**
 * Created by S-Shimotori on 6/3/16.
 */

class ListNoItemAdapter(context: Context, objects: List<String>): ArrayAdapter<String>(context, 0, objects) {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View?

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_message_item, parent, false)
        } else {
            view = convertView
        }

        val messageTextView = view!!.findViewById(R.id.MessageTextView) as TextView
        messageTextView.text = getItem(position)
        return view
    }
}