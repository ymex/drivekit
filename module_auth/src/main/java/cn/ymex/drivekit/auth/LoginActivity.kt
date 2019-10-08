package cn.ymex.drivekit.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import cn.ymex.drivekit.api.http.RetrofitManager
import cn.ymex.drivekit.api.third.addEncode
import cn.ymex.drivekit.auth.viewmodel.LoginVMFactory
import cn.ymex.drivekit.auth.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.FormBody
import okhttp3.Headers
import java.net.URLEncoder
import java.util.*

class LoginActivity : AppCompatActivity() {


    private val loginViewModel:LoginViewModel by viewModels { LoginVMFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        loginViewModel.toastLiveData.observe(this, Observer<String> {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        btnLogin.setOnClickListener {
            loginViewModel.login(etAccount.text.toString(),etPassword.text.toString())
        }
    }
}
