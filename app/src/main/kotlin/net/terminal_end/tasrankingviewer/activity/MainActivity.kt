package net.terminal_end.tasrankingviewer.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.astuetz.PagerSlidingTabStrip
import net.terminal_end.tasrankingviewer.R
import net.terminal_end.tasrankingviewer.fragment.ListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.MainViewPager) as ViewPager
        val listFragmentPagerAdapter = ListFragment.ListFragmentPagerAdapter(supportFragmentManager)
        viewPager.adapter = listFragmentPagerAdapter

        val pagerSlidingTabStrip = findViewById(R.id.MainViewPagerTab) as PagerSlidingTabStrip
        pagerSlidingTabStrip.setViewPager(viewPager)
    }
}
