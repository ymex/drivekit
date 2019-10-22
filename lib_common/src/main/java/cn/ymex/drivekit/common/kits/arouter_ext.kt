package cn.ymex.drivekit.common.kits

import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 便捷方法
 */
fun aRouter(path: String): Postcard {
    return ARouter.getInstance().build(path)
}

/**
 * 补全信息，通过completion ，将查找 path 对应的配置
 */
fun Postcard.completion(): Postcard {
    LogisticsCenter.completion(this)
    return this
}