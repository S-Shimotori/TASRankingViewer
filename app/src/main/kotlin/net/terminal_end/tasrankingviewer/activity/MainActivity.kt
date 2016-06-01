package net.terminal_end.tasrankingviewer.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.astuetz.PagerSlidingTabStrip
import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.fragment.ListFragment
import net.terminal_end.tasrankingviewer.model.LastModified
import net.terminal_end.tasrankingviewer.util.toCalendar
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.MainViewPager) as ViewPager
        val listFragmentPagerAdapter = ListFragment.ListFragmentPagerAdapter(supportFragmentManager)
        viewPager.adapter = listFragmentPagerAdapter

        val pagerSlidingTabStrip = findViewById(R.id.MainViewPagerTab) as PagerSlidingTabStrip
        pagerSlidingTabStrip.setViewPager(viewPager)
        pagerSlidingTabStrip.indicatorColor = Color.rgb(0, 155, 159)

        val client = OkHttpClient()
        val request = Request.Builder().url(resources.getString(R.string.endpoint_last_modified)).build()
        val lastModifiedTextView = findViewById(R.id.LastModifiedTextView) as TextView
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                lastModifiedTextView.text = resources.getString(R.string.fail)
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response != null && response.isSuccessful) {
                    val lastModified = Gson().fromJson(response.body().string(), LastModified::class.java)
                    val calendarResult = lastModified.last_modified.toCalendar()
                    if (calendarResult.component1() != null) {
                        lastModifiedTextView.text = createLastModifiedMessage(calendarResult.get())
                        return
                    }
                }
                lastModifiedTextView.text = resources.getString(R.string.fail)
            }
        })
    }

    fun createLastModifiedMessage(calendar: Calendar): String {
        return calendar.get(Calendar.YEAR).toString() +
                resources.getString(R.string.year) +
                (calendar.get(Calendar.MONTH) + 1).toString() +
                resources.getString(R.string.month) +
                calendar.get(Calendar.DATE).toString() +
                resources.getString(R.string.date) +
                calendar.get(Calendar.HOUR_OF_DAY).toString() +
                resources.getString(R.string.hour) +
                calendar.get(Calendar.MINUTE).toString() +
                resources.getString(R.string.minute) +
                calendar.get(Calendar.SECOND).toString() +
                resources.getString(R.string.second)
    }
}
