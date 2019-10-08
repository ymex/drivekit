package cn.ymex.drivekit.api.repository

import cn.ymex.drivekit.api.HttpResultData
import cn.ymex.drivekit.api.repository.auth.AuthApiService
import cn.ymex.drivekit.api.repository.vo.AccountParam
import cn.ymex.drivekit.api.repository.vo.LoginResultEnt
import cn.ymex.drivekit.api.third.asyncIo
import io.reactivex.Observable
import retrofit2.Retrofit

/**
 * Created by ymex on 2019/9/27.
 * About:
 */
class AuthRepos(val retrofit: Retrofit) {

    fun login(name:String,password:String) : Observable<HttpResultData<LoginResultEnt>> {
        return retrofit.create(AuthApiService::class.java).login(AccountParam(name,password)).asyncIo()
    }
}