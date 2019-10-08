package cn.ymex.drivekit.auth.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.ymex.drivekit.api.http.RetrofitManager
import cn.ymex.drivekit.api.repository.AuthRepos
import cn.ymex.drivekit.api.repository.vo.AccountParam
import cn.ymex.drivekit.api.third.RxViewModel
import cn.ymex.drivekit.api.third.asLife

/**
 * Created by ymex on 2019/9/28.
 * About:
 */
class LoginViewModel(val authRepos: AuthRepos) : RxViewModel() {

    private val _toastLiveData = MutableLiveData<String>()
    val toastLiveData = _toastLiveData

    @SuppressLint("CheckResult")
    fun login(account: String, password: String) {
        if (account.isEmpty() || password.isEmpty() || password.length != 6) {
            _toastLiveData.value = "用户名或密码错误"
            return
        }
        authRepos.login(account, password).asLife(this)
            .subscribe({
                _toastLiveData.value = it.msg
            }) {
                _toastLiveData.value = it.localizedMessage
            }
    }

}

/**
 * Factory for [LoginVMFactory].
 */
object LoginVMFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(AuthRepos(RetrofitManager.instance.mRetrofit)) as T
    }
}