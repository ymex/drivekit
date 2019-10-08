package cn.ymex.drivekit.api.repository

import cn.ymex.drivekit.api.http.RetrofitManager
import cn.ymex.drivekit.api.third.asyncIo
import cn.ymex.drivekit.api.module.getMainApiService
import com.google.gson.JsonObject
import io.reactivex.Observable

/**
 * Created by ymex on 2019/9/27.
 * About:
 */
class HomeDataRepos {

    fun loadHotBanner():Observable<JsonObject> {
        return RetrofitManager.instance.getMainApiService()
            .banner()
            .asyncIo()
    }
}