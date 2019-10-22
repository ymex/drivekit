package cn.ymex.drivekit.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.ymex.drivekit.api.http.RetrofitManager
import cn.ymex.drivekit.api.third.asyncIo
import cn.ymex.drivekit.api.module.getMainApiService
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "/mod_main/start")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener {
            // ARouter.getInstance().build("/mod_main/param").withString("paramName","ymex").navigation(this,1000)


            ARouter.getInstance().build("/mod_main/param")
                .withString("paramName", "ymex")
                .navigation(this, 1000, object : NavigationCallback {
                    override fun onLost(postcard: Postcard?) {
                        println("----------------:onLost ${postcard.toString()}")
                    }

                    override fun onFound(postcard: Postcard?) {
                        println("----------------:onFound ${postcard.toString()}")
                    }

                    override fun onInterrupt(postcard: Postcard?) {
                        println("----------------:onInterrupt ${postcard.toString()}")
                    }

                    override fun onArrival(postcard: Postcard?) {
                        println("----------------:onArrival ${postcard.toString()}")
                    }
                })
        }


        val d = RetrofitManager.instance.getMainApiService()
            .banner()
            .asyncIo()
            .subscribe({

                println("----------------------:::json: ${it.toString()}")
            }) {
                it.printStackTrace()
                println("----------------------:::error: ${it.localizedMessage}")
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            println("------------onActivityResult:" + data?.getStringExtra("param_name"))
        }
    }


}
