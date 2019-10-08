package cn.ymex.drivekit.api.repository.auth

import cn.ymex.drivekit.api.HttpResultData
import cn.ymex.drivekit.api.repository.vo.AccountParam
import cn.ymex.drivekit.api.repository.vo.LoginResultEnt
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by ymex on 2019/9/27.
 * About:
 */
interface AuthApiService {

    @POST("/auth/login/body")
    fun login(@Body account: AccountParam): Observable<HttpResultData<LoginResultEnt>>
}