package cn.ymex.drivekit.common.app

import android.app.Application
import androidx.annotation.IntRange

/**
 * Created by ymex on 2019/9/25.
 * About:module Application的生命周期
 */
abstract class IModelAppContext(val moduleApp: Application) {

    /**
     * 获取优先级，priority 越大，优先级越高
     */

    @IntRange(from = 0)
    var priority = 0

    abstract fun onCreate()

    abstract fun onTerminate()


}