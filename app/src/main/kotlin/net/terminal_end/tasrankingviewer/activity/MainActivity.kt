package net.terminal_end.tasrankingviewer.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.astuetz.PagerSlidingTabStrip
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.fragment.ListFragment
import okhttp3.*
import java.io.IOException

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
        val request = Request.Builder().url(resources.getString(R.string.last_modified)).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                throw UnsupportedOperationException()
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response != null && response.isSuccessful) {
                    Log.d("MainActivity", response.body().string())
                }
            }
        })
    }
}
