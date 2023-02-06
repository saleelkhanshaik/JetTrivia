package com.example.jettrivia.repository

import android.util.Log
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionAPI
import javax.inject.Inject

class QuestionsRepository @Inject constructor(private val questionAPI: QuestionAPI) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()
    suspend fun getAllQuestionsData(size: Int): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loadingStatus = true
            dataOrException.response = questionAPI.getAllQuestions(200).results
            Log.d("QuestionsRepository", "getAllQuestionsData: ${dataOrException.response}")
            if(dataOrException.response.toString().isEmpty()){
                dataOrException.e = Exception("No Data Available")
            }
            dataOrException.loadingStatus = false
        } catch (exception: Exception) {
            dataOrException.loadingStatus = false
            dataOrException.e = exception
        }
        return dataOrException
    }
}