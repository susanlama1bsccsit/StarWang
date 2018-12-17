package com.susan.org.gameifydemo.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author Susan Lama on 16/12/2018.
 * @Email susan.invents@gmail.com
 */
object RetrofitApiClient {

    private val BASE_URL = "https://firebasestorage.googleapis.com/"
    internal val QUESTION_URL = "v0/b/news-excercise.appspot.com/o/game.json?alt=media&token=3c3611b8-29b5-4100-ad18-663f80936a60"

    fun create(): ApiService {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}
