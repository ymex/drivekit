package cn.ymex.drivekit.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.ymex.kitx.kits.ActivityStack
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_param.*


@Route(path = "/mod_main/param")
class ParamActivity : AppCompatActivity() {

    @Autowired
    @JvmField
    public var paramName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_param)
        ARouter.getInstance().inject(this)
        println("--------------Autowired:$paramName")
        println("--------------Autowired: "+ActivityStack.get().toString())
        button2.setOnClickListener {
            println("--------:::"+ActivityStack.get().toString())
            setResult(1000, Intent().putExtra("param_name", "hellow !! $paramName"))
            ActivityStack.get().finishTop()
            //finish()
        }
    }
}
