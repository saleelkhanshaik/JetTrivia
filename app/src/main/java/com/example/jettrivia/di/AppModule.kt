package com.example.jettrivia.di

import com.example.jettrivia.network.QuestionAPI
import com.example.jettrivia.repository.QuestionsRepository
import com.example.jettrivia.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesQuestionRepositoryAPI(api: QuestionAPI):QuestionsRepository{
        return QuestionsRepository(api)
    }

    @Singleton
    @Provides
    fun provideQuestionAPI(): QuestionAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(QuestionAPI::class.java)
    }
}