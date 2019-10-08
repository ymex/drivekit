package cn.ymex.drivekit.api.repository.vo
import com.google.gson.annotations.SerializedName




data class LoginResultEnt(
    @SerializedName("account")
    val account: String = "",
    @SerializedName("lazy")
    val lazy: Int = 0,
    @SerializedName("password")
    val password: String = ""
)