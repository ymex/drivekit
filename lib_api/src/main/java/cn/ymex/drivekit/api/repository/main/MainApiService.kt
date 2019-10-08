package cn.ymex.drivekit.api.repository.main

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by ymex on 2019/9/27.
 * About:
 */
interface MainApiService {

    @GET("/api/public/?service=Home.getHot")
    fun banner(): Observable<JsonObject>
}