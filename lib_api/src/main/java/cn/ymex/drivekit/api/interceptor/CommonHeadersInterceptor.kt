package cn.ymex.drivekit.api.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by ymex on 2017/9/28.
 * 公共头部添加拦截器
 */
class CommonHeadersInterceptor(val headers: Headers.Builder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        println("--------------------CommonHeadersInterceptor hhhh:")
        val request = chain.request()

        val req = request.newBuilder().method(request.method, request.body)

        val hs = headers.build()

        hs.names().forEach {
            req.addHeader(it, hs[it] ?:"")
        }

        return chain.proceed(req.build())
    }
}