package com.susan.org.gameifydemo.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * @Author Susan Lama on 16/12/2018.
 * @Email susan.invents@gmail.com
 */
class Question : Serializable {

    @SerializedName("correctAnswerIndex")
    var correctAnswerIndex: String? = null

    @SerializedName("imageUrl")
    var imageUrl: String? = null

    @SerializedName("standFirst")
    var standFirst: String? = null

    @SerializedName("storyUrl")
    var storyUrl: String? = null

    @SerializedName("section")
    var section: String? = null

    @SerializedName("headlines")
    var headlines: List<String> = arrayListOf()

//    constructor(
//        correctAnswerIndex: String?,
//        imageUrl: String?,
//        standFirst: String?,
//        storyUrl: String?,
//        section: String?,
//        headlines: List<String>
//    ) {
//        this.correctAnswerIndex = correctAnswerIndex
//        this.imageUrl = imageUrl
//        this.standFirst = standFirst
//        this.storyUrl = storyUrl
//        this.section = section
//        this.headlines = headlines
//    }
}
