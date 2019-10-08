package cn.ymex.drivekit.api


/**
 * 通信实体
 * index 建议为 详细信息说明
 */

class EventMessage<T>(val index: String) {

    var data: T? = null


    fun eq(other: EventMessage<T>): Boolean {
        return index == other.index
    }

    fun eq(other: String): Boolean {
        return index == other
    }
}