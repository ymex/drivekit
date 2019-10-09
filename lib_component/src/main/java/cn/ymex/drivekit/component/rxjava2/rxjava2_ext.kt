package cn.ymex.drivekit.component.rxjava2

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * 线程调度
 */
fun <T> Observable<T>.asyncIo(): Observable<T> {
    return compose {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Observable<T>.asyncThread(): Observable<T> {
    return compose {
        it.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
