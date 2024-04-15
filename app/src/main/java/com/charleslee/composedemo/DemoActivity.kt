package com.charleslee.composedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.charleslee.composedemo.util.StatusBarUtil
import com.google.android.material.tabs.TabLayout


/**
 *
 * <p> Created by CharlesLee on 2024/3/13
 * 15708478830@163.com
 */
class DemoActivity : BaseActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private val fragments = mutableListOf<FlipTabFragment>()

    private val pagerAdapter = object : FragmentPagerAdapter(this.supportFragmentManager) {
        override fun getCount() = titles.size

        override fun getItem(position: Int) = fragments[position]

        override fun getPageTitle(position: Int) = titles[position]
    }

    private val titles = arrayOf(
        "欧冠",
        "英超",
        "西甲",
        "意甲",
        "德甲",
        "法甲",
        "中超",
        "欧联杯",
        "皇马",
        "巴萨",
        "拜仁",
        "利物浦"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        showImmersiveStatusBar(false)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        for (element in titles) {
            fragments.add(FlipTabFragment())
        }

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun showImmersiveStatusBar(isDarkTheme: Boolean, isFitWindow: Boolean = false) {
        if (!StatusBarUtil.isShowImmersion()) {
            return
        }
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, isFitWindow)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, isDarkTheme)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000)
        }

        runOnUiThread {  }
    }

    fun getTabLayout() = tabLayout

    fun getViewPager() = viewPager
}