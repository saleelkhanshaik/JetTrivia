package com.example.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionsRepository: QuestionsRepository) :
    ViewModel() {
    val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))
    init {
        getAllQuestions(6)
    }
    private fun getAllQuestions(size:Int){
        viewModelScope.launch {
            data.value.loadingStatus = true
            data.value = questionsRepository.getAllQuestionsData(size)
            data.value.response?.forEach {
                it.incorrect_answers.add(it.correct_answer)
            }
            if(data.value.response.toString().isNotEmpty()){
                data.value.loadingStatus = false
            }else{
                data.value.loadingStatus = false
                data.value.e = data.value.e
            }
        }
    }
}