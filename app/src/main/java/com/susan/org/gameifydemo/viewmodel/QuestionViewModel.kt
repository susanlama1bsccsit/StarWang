package com.susan.org.gameifydemo.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.susan.org.gameifydemo.data.RetrofitApiClient
import com.susan.org.gameifydemo.model.BaseModel
import com.susan.org.gameifydemo.model.Question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @Author Susan Lama on 16/12/2018.
 * @Email susan.invents@gmail.com
 */
class QuestionViewModel : ViewModel() {

    //this is the data that we will fetch asynchronously
    private var questionList: MutableLiveData<List<Question>>? = null

    //we will call this method to get the data
    fun getQuestionList(): LiveData<List<Question>> {
        //if the list is null
        if (questionList == null) {
            questionList = MutableLiveData()
            //we will load it asynchronously from server in this method
            loadQuestiones()
        }

        //finally we will return the list
        return questionList as MutableLiveData<List<Question>>
    }

    //This method is using Retrofit to get the JSON data from URL
    private fun loadQuestiones() {

        val apiService = RetrofitApiClient.create()
        val call = apiService.fetchQuestion(RetrofitApiClient.QUESTION_URL)
        call.enqueue(object : Callback<BaseModel> {
            override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {

                //finally we are setting the list to our MutableLiveData
                if (response.isSuccessful){
                    Log.d("SUSANTEST", "Response Success ${response.body()!!.questionList[0].headlines[0]}")
                    questionList!!.postValue(response.body()!!.questionList)

                }


            }

            override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                Log.d("SUSAN", "Response failed")
            }
        })
    }
}
