package cn.ymex.drivekit.common.life

import androidx.annotation.NonNull

/**
 * module Application的生命周期
 */
object AppLifeCycleManager {
    private val sPinLifeCycleList = mutableListOf<AppLifeCycle>()


    fun <T : AppLifeCycle> register(@NonNull lifeCycle: T) {
        if (!sPinLifeCycleList.contains(lifeCycle)) {
            sPinLifeCycleList.add(lifeCycle)
        }
    }


    fun <T : AppLifeCycle> unregister(@NonNull lifeCycle: T) {
        if (sPinLifeCycleList.isNotEmpty()) {
            if (sPinLifeCycleList.contains(lifeCycle)) {
                lifeCycle.onTerminate()
                sPinLifeCycleList.remove(lifeCycle)
            }
        }
    }


    fun execute() {
        if (sPinLifeCycleList.isNotEmpty()) {
            // 冒泡算法排序，按优先级从高到低重新排列组件生命周期
            var temp: AppLifeCycle
            var i = 0
            val len = sPinLifeCycleList.size - 1
            while (i < len) {
                for (j in 0 until len - i) {
                    if (sPinLifeCycleList[j].priority < sPinLifeCycleList[j + 1].priority) {
                        temp = sPinLifeCycleList[j]
                        sPinLifeCycleList[j] = temp
                        sPinLifeCycleList[j + 1] = temp
                    }
                }
                i++
            }
            for (lifeCycle in sPinLifeCycleList) {
                lifeCycle.onCreate()
            }
        }
    }



    fun clear() {
        if (sPinLifeCycleList.isNotEmpty()) {
            sPinLifeCycleList.forEach {
                it.onTerminate()
            }

            sPinLifeCycleList.clear()
        }
    }
}