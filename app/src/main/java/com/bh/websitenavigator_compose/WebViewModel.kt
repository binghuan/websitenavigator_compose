package com.bh.websitenavigator_compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class WebViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _urls = MutableStateFlow<List<String>>(emptyList())
    val urls: StateFlow<List<String>> get() = _urls

    private val apiService: ApiService by lazy {
        Retrofit.Builder().baseUrl("https://binghuan.github.io/download/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    fun fetchUrls() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = apiService.getPages()
            _urls.value = response.items.map { it.url }
            _isLoading.value = false
        }
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}

interface ApiService {
    @GET("private-58ab56-mocks3_apiary-mock_com_pages.json")
    suspend fun getPages(): ApiResponse
}

data class ApiResponse(val items: List<PageItem>)

data class PageItem(val url: String)
