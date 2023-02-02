package com.example.jettrivia.network

import com.example.jettrivia.model.QuestionsData
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton
//const val BASE_URL:String ="https://opentdb.com/api.php?amount=50"
@Singleton
interface QuestionAPI {
    @GET("api.php?")
    suspend fun getAllQuestions(@Query("amount") size: Int): QuestionsData
}