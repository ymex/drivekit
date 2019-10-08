package cn.ymex.drivekit.api.http

import cn.ymex.drivekit.api.Api
import cn.ymex.drivekit.api.interceptor.CommonHeadersInterceptor
import cn.ymex.drivekit.api.interceptor.CommonParamsInterceptor
import cn.ymex.kitx.kits.SSLKit
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


/**
 * Created by ymex on 2019/9/25.
 * About:
 */
class RetrofitManager private constructor() {

    var mRetrofit: Retrofit
    private var okHttpBuilder: OkHttpClient.Builder
    var initCommonHeaders = false
    var initCommonParams = false


    companion object {
        val instance: RetrofitManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
    }

    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        //信任所有证书
        val sslParams = SSLKit.genSSLParams(null, null, null)
        return OkHttpClient.Builder()
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .retryOnConnectionFailure(true)
            .dispatcher(Dispatcher())
            .addInterceptor(HttpLoggingInterceptor())
            .sslSocketFactory(sslParams.sslSocketFactory, sslParams.trustManager)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    }


    init {
        okHttpBuilder = getOkHttpClientBuilder()
        mRetrofit = getRetrofit()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().client(okHttpBuilder.build()).baseUrl(Api.baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    /**
     * 公共参数
     */
    fun commonParams(params: FormBody.Builder) {
        if (initCommonParams) {
            throw IllegalArgumentException("already set common params!!")
        }
        initCommonParams = true
        okHttpBuilder.addInterceptor(CommonParamsInterceptor(params))
        mRetrofit = getRetrofit()
    }


    /**
     * 公共头部
     */
    fun commonHeaders(headers: Headers.Builder) {
        if (initCommonHeaders) {
            throw IllegalArgumentException("already set common headers!!")
        }
        initCommonHeaders = true
        okHttpBuilder.addInterceptor(CommonHeadersInterceptor(headers))
        mRetrofit = getRetrofit()
    }

}