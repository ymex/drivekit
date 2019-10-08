package cn.ymex.drivekit.api.interceptor

import okhttp3.*

/**
 * Created by ymex on 2017/9/28.
 * 公共参数添加拦截器
 */
class CommonParamsInterceptor(val _params: FormBody.Builder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.body != null) {
            when (request.body) {
                is FormBody -> {

                    val rb = FormBody.Builder()
                    val body = request.body as FormBody
                    for (index in 0 until body.size) {
                        rb.add(body.name(index), body.value(index))
                    }
                    val params = _params.build()
                    if (params.size > 0) {
                        for (index in 0 until params.size) {
                            rb.add(params.name(index), params.value(index))
                        }
                    }

                    val req = request.newBuilder().method(request.method, rb.build())

                    return chain.proceed(req.build())
                }
                is MultipartBody -> {

                    val rb = MultipartBody.Builder()

                    val body = request.body as MultipartBody
                    body.parts.forEach {
                        rb.addPart(it.headers, it.body)
                    }

                    val params = _params.build()
                    if (params.size > 0) {
                        for (index in 0 until params.size) {
                            rb.addFormDataPart(params.name(index), params.value(index))
                        }
                    }
                    val req = request.newBuilder().method(request.method, rb.build())

                    return chain.proceed(req.build())

                }
                else -> {

                    return chain.proceed(unknowRequest(request))
                }
            }
        } else {

            return chain.proceed(unknowRequest(request))
        }

    }


    /**
     * 未知requestbody
     */
    fun unknowRequest(request: Request): Request {
        val url = request.url.newBuilder()
        val params = _params.build()
        if (params.size > 0) {
            for (index in 0 until params.size) {
                url.addQueryParameter(params.name(index), params.value(index))
            }
        }
        println("--------------url:"+url.build())
        val builder = request.newBuilder().url(url.build())
        return builder.method(request.method, request.body)
            .build()
    }

}