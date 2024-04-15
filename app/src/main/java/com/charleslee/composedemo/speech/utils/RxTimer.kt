package com.charleslee.composedemo.speech.utils

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * Desc: Rx 轮询定时器
 * @author CharlesLee
 * @date   2020/4/28 16:17
 * @email  15708478830@163.com
 **/
object RxTimer {
    private var mTimerDisposable: Disposable? = null
    private var mIntervalDisposable: Disposable? = null

    /**
     * 定时器
     * milliseconds毫秒后执行next操作
     */
    fun timer(milliseconds: Long, doNext: (Long) -> Unit) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(disposable: Disposable) {
                        mTimerDisposable = disposable
                    }

                    override fun onNext(number: Long) {
                        doNext.invoke(number)
                    }

                    override fun onComplete() { //取消订阅
                        cancelTimer()
                    }

                    override fun onError(e: Throwable) {
                        cancelTimer()
                    }
                })
    }


    /**
     * 轮询器
     * 每隔milliseconds毫秒后执行next操作
     */
    fun interval(milliseconds: Long, doNext: (Long) -> Unit) {
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long?> {
                    override fun onSubscribe(disposable: Disposable) {
                        mIntervalDisposable = disposable
                    }

                    override fun onNext(number: Long) {
                        doNext.invoke(number)
                    }

                    override fun onComplete() {}

                    override fun onError(e: Throwable) {}
                })
    }

    /**
     * 取消定时器
     */
    fun cancelTimer() {
        mTimerDisposable?.takeIf {
            !it.isDisposed
        }?.apply {
            dispose()
        }
    }

    /**
     * 取消轮询器
     */
    fun cancelInterval() {
        mIntervalDisposable?.takeIf {
            !it.isDisposed
        }?.apply {
            dispose()
        }
    }
}