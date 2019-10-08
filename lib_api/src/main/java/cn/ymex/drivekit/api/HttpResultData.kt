package cn.ymex.drivekit.api

import com.google.gson.annotations.SerializedName


/**
 * Created by ymex on 2019/9/27.
 * About:
 */
data class HttpResultData<T>(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("data")
    val data: T,
    @SerializedName("message")
    val msg: String = ""
)