package com.example.jettrivia.repository

import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.ResultsItem
import com.example.jettrivia.network.QuestionAPI
import javax.inject.Inject

class QuestionsRepository @Inject constructor(private val questionAPI: QuestionAPI) {
    private val dataOrException = DataOrException<ArrayList<ResultsItem>, Boolean, Exception>()
    suspend fun getAllQuestionsData(size: Int): DataOrException<ArrayList<ResultsItem>, Boolean, Exception> {
        try {
            dataOrException.loadingStatus = true
            dataOrException.response = questionAPI.getAllQuestions(size).results
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