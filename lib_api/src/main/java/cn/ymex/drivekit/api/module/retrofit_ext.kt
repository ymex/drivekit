package cn.ymex.drivekit.api.module

import cn.ymex.drivekit.api.http.RetrofitManager
import cn.ymex.drivekit.api.repository.auth.AuthApiService
import cn.ymex.drivekit.api.repository.main.MainApiService

/**
 * Created by ymex on 2019/9/27.
 * About:
 */

fun RetrofitManager.getMainApiService(): MainApiService {
    return  mRetrofit.create(MainApiService::class.java)
}

fun RetrofitManager.getAuthApiService(): AuthApiService {
    return mRetrofit.create(AuthApiService::class.java)
}

