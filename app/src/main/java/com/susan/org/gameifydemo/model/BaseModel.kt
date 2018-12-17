package com.susan.org.gameifydemo.model

import android.arch.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * @Author Susan Lama on 16/12/2018.
 * @Email susan.invents@gmail.com
 */
class BaseModel : Serializable {

    @SerializedName("product")
    var product: String? = null

    @SerializedName("resultSize")
    var resultSize: String? = null

    @SerializedName("version")
    var version: String? = null

    @SerializedName("items")
    var questionList: List<Question> = arrayListOf()
}
