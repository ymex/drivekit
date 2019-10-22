package cn.ymex.drivekit.common.app

import androidx.annotation.NonNull

/**
 * module Application的生命周期
 */
object ModelAppContextManager {
    private val modelAppContextList = mutableListOf<IModelAppContext>()


    fun <T : IModelAppContext> register(@NonNull lifeCycle: T) {
        if (!modelAppContextList.contains(lifeCycle)) {
            modelAppContextList.add(lifeCycle)
        }
    }


    fun <T : IModelAppContext> unregister(@NonNull lifeCycle: T) {
        if (modelAppContextList.isNotEmpty()) {
            if (modelAppContextList.contains(lifeCycle)) {
                lifeCycle.onTerminate()
                modelAppContextList.remove(lifeCycle)
            }
        }
    }


    fun execute() {
        if (modelAppContextList.isNotEmpty()) {
            // 冒泡算法排序，按优先级从高到低重新排列组件生命周期
            var temp: IModelAppContext
            var i = 0
            val len = modelAppContextList.size - 1
            while (i < len) {
                for (j in 0 until len - i) {
                    if (modelAppContextList[j].priority < modelAppContextList[j + 1].priority) {
                        temp = modelAppContextList[j]
                        modelAppContextList[j] = temp
                        modelAppContextList[j + 1] = temp
                    }
                }
                i++
            }
            for (lifeCycle in modelAppContextList) {
                lifeCycle.onCreate()
            }
        }
    }



    fun clear() {
        if (modelAppContextList.isNotEmpty()) {
            modelAppContextList.forEach {
                it.onTerminate()
            }

            modelAppContextList.clear()
        }
    }
}