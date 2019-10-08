package cn.ymex.drivekit.common.kits

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter


fun aRouter(path: String): Postcard {
    return ARouter.getInstance().build(path)
}