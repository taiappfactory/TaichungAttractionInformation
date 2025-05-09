package com.tai.taichungattractioninformation.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // 台中公開資訊站 API 的 Retrofit 實例
    private const val BASE_URL = "https://datacenter.taichung.gov.tw/swagger/OpenData/"

    private val loggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        Log.d("API-Request", "🔹 URL: ${request.url}")
        Log.d("API-Request", "🔹 Method: ${request.method}")
        Log.d("API-Request", "🔹 Headers:\n${request.headers}")

        val response = chain.proceed(request)

        val responseBody = response.body
        val contentType = responseBody?.contentType()
        val responseString = responseBody?.string()

        Log.d("API-Response", "🔸 Code: ${response.code}")
        Log.d("API-Response", "🔸 Message: ${response.message}")
        Log.d("API-Response", "🔸 Headers:\n${response.headers}")
        Log.d("API-Response", "🔸 Body:\n$responseString")

        // 重新建立 response.body（只能被讀一次）
        val newResponseBody = ResponseBody.create(contentType, responseString ?: "")
        response.newBuilder().body(newResponseBody).build()
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
