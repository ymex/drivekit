package cn.ymex.drivekit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 启动屏
 */
class LauncherActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentView = View.inflate(this,R.layout.activity_launcher,null)
        setContentView(contentView)


        val animation = AlphaAnimation(0.6f, 1.0f)
        animation.duration = 1000L
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                println("------------------动画完成")
                ARouter.RAW_URI
                ARouter.getInstance().build("/mod_main/start").navigation()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        contentView.startAnimation(animation)

    }
}
