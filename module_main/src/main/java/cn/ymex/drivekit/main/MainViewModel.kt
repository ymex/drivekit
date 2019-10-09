package cn.ymex.drivekit.main

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.ymex.drivekit.api.http.RetrofitManager
import cn.ymex.drivekit.api.third.RxViewModel
import cn.ymex.drivekit.api.third.asLife
import cn.ymex.drivekit.api.third.asyncIo
import cn.ymex.drivekit.api.module.getMainApiService
import com.google.gson.JsonObject

/**
 * Created by ymex on 2019/9/27.
 * About:
 */
class MainViewModel : RxViewModel() {

    private val _mainData: MutableLiveData<JsonObject> = MutableLiveData()
    val mainData: LiveData<JsonObject> = _mainData

    @SuppressLint("CheckResult")
    fun load() {
        RetrofitManager.instance.getMainApiService()
            .banner()
            .asyncIo()
            .asLife(this)
            .subscribe({
                _mainData.value = it
            }) {
                println(it.localizedMessage)
            }
    }

}