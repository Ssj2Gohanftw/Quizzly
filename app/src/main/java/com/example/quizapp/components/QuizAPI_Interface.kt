import com.example.quizapp.components.QuizQuestion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface QuizApiService {
    @GET("/api/v1/questions")
    suspend fun getQuizQuestions(): List<QuizQuestion>
}

object RetrofitClient {
    private const val BASE_URL = "https://quizapi.io/api/v1/"

    val instance: QuizApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuizApiService::class.java)
    }
}