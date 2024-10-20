package com.example.quizapp.components
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



interface QuizApiService {

    @GET("questions")
    fun getQuestions(
        @Query("apiKey") apiKey: String,
        @Query("limit") limit: Int = 10,
        @Query("category") category: String?,
        @Query("difficulty") difficulty: String
    ): List<QuizQuestion>
}
object QuizApiClient {

    private const val BASE_URL = "https://quizapi.io/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Optional, for logging requests
        .build()

    val api: QuizApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuizApiService::class.java)
    }
}