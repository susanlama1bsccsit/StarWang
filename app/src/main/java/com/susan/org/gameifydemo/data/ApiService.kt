package com.susan.org.gameifydemo.data

import com.susan.org.gameifydemo.model.BaseModel
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.Call

/**
 * @Author Susan Lama on 16/12/2018.
 * @Email susan.invents@gmail.com
 */
interface ApiService {

    @GET
    fun fetchQuestion(@Url url: String): Call<BaseModel>

}
