package cn.ymex.drivekit.api.third

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ymex on 2019/9/27.
 * About:
 */

open class RxViewModel :ViewModel(){
    val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}