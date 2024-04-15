package com.charleslee.composedemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.charleslee.composedemo.flip.FlipView
import com.charleslee.composedemo.flip.OverFlipMode


/**
 *
 * <p> Created by CharlesLee on 2024/3/13
 * 15708478830@163.com
 */
class FlipTabFragment : Fragment(), FlipAdapter.Callback, FlipView.OnFlipListener,
    FlipView.OnOverFlipListener {
    private lateinit var mFlipView: FlipView
    private lateinit var mAdapter: FlipAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_flip_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFlipView = view.findViewById<View>(R.id.flip_view) as FlipView
        mAdapter = FlipAdapter(false)
        mAdapter.setCallback(this)
        mFlipView.setAdapterWithTabLayout(mAdapter, (requireActivity() as DemoActivity).getTabLayout())
        mFlipView.setOnFlipListener(this)
        mFlipView.setOverFlipMode(OverFlipMode.GLOW)
        mFlipView.setEmptyView(view.findViewById(R.id.empty_view))
        mFlipView.setOnOverFlipListener(this)
    }

    override fun onPageRequested(page: Int) {
//        mFlipView.smoothFlipTo(page)
        if (page == 0) {
//            mFlipView.peakPrevious()
            mFlipView.backToTop()
        } else {
            mFlipView.peakNext()
        }
    }

    override fun onFlipping(v: FlipView?) {
        Log.i("charleslee", "onFlipping...")
//        (requireActivity() as DemoActivity).getTabLayout().visibility = View.GONE
    }

    override fun onFlippedToPage(v: FlipView?, position: Int, id: Long) {
        Log.i("charleslee", "Page: $position")
//        (requireActivity() as DemoActivity).getTabLayout().visibility = View.VISIBLE

//        if (position > mFlipView.pageCount - 3 && mFlipView.pageCount < 30) {
//            mAdapter.addItems()
//        }
    }

    override fun onOverFlip(
        v: FlipView?,
        mode: OverFlipMode?,
        overFlippingPrevious: Boolean,
        overFlipDistance: Float,
        flipDistancePerPage: Float
    ) {
        Log.i("charleslee", "overFlipDistance = $overFlipDistance" + ", overFlippingPrevious = " + overFlippingPrevious)
    }
}