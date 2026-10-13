package com.example.studentmanagementapp.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://127.0.0.1:8000/"

    @Volatile
    private var retrofit: Retrofit? = null

    private fun getRetrofit(tokenStorage: TokenStorage): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: run {
                val client = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(tokenStorage))
                    .build()

                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .also { retrofit = it }
            }
        }
    }

    fun studentApi(tokenStorage: TokenStorage): StudentApi =
        getRetrofit(tokenStorage).create(StudentApi::class.java)

    fun authApi(tokenStorage: TokenStorage): AuthApi =
        getRetrofit(tokenStorage).create(AuthApi::class.java)
}
