package cn.ymex.drivekit.main.provider

import android.app.Application
import android.content.Context
import cn.ymex.drivekit.common.app.IModelAppContext
import cn.ymex.drivekit.common.provider.AppContextProvider
import cn.ymex.drivekit.main.MainModelAppContext
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/provider/life/main", name = "module_main")
class ImpAppContextService : AppContextProvider {
    override fun getModelAppContext(application: Application): IModelAppContext {
        return MainModelAppContext(application)
    }

    override fun init(context: Context) {

    }
}