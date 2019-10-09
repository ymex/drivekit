package cn.ymex.drivekit.component.okhttp3

import java.net.URLEncoder

/**
 * okhttp3 header为中文时需要URLEncoder转码
 */
fun okhttp3.Headers.Builder.addEncode(name: String, value: String) = apply {
    add(name, URLEncoder.encode(value, "UTF-8"))
}