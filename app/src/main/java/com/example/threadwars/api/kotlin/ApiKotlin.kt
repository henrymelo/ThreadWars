package com.example.threadwars.api.kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class ApiKotlin : ViewModel() {
    private val _apiData = MutableLiveData<String>()
    val apiData: LiveData<String> get() = _apiData

    private val API_URL = "https://jsonplaceholder.typicode.com/"

    interface ApiService {
        @GET("posts/1")
        suspend fun getApiData(): String
    }

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Método para iniciar o processo de chamada da API usando Coroutines
    fun fetchDataFromApi() {
        Timber.w("*****startTime: ${dateTimeString()}")
        viewModelScope.launch {
            try {
                val apiResponse = withContext(Dispatchers.IO) {
                    apiService.getApiData()
                }
                Timber.w("*****endTime: ${dateTimeString()}")
                _apiData.value = apiResponse
            } catch (e: Exception) {
                Timber.w("*****endTimeException: ${dateTimeString()}")
                // Tratamento de exceções, por exemplo, enviando um evento de erro para a interface do usuário
            }
        }
    }

    // Função para realizar uma chamada simulada à API
    private suspend fun getApiResponseFromServer(): String {
        Timber.w("****TSTe: ${dateTimeString()}")

        return "Dados da API"
    }

    data class ApiDataEvent(val apiData: String)

    // Adicionando função para obter a data e hora atual formatada
    private fun dateTimeString(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        return sdf.format(date)
    }
}
