package cn.ymex.drivekit.api.repository.vo
import com.google.gson.annotations.SerializedName


data class SlideWarp(  @SerializedName("slide")
                       val slide: List<SlideEnt>)

data class SlideEnt(
    @SerializedName("need_token")
    val needToken: Int = 0,
    @SerializedName("slide_content")
    val slideContent: String = "",
    @SerializedName("slide_name")
    val slideName: String = "",
    @SerializedName("slide_pic")
    val slidePic: String = "",
    @SerializedName("slide_title")
    val slideTitle: String = "",
    @SerializedName("slide_url")
    val slideUrl: String = ""
)