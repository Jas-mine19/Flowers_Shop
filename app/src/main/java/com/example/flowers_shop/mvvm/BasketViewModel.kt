package com.example.flowers_shop.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowers_shop.data.Api
import com.example.flowers_shop.data.Basket
import com.example.flowers_shop.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class BasketViewModel : ViewModel() {
    private val apiInterface = Api().create()

    private val _basketLiveData = MutableLiveData<Boolean>()
    val basketLiveData = _basketLiveData

    private val _errorBasketLiveData = MutableLiveData<String>()
    val errorBasketLiveData = _errorBasketLiveData


    fun createBasket(basket: Basket) {
        viewModelScope.launch {
            getResponse(basket)
        }
    }

    private suspend fun getResponse(basket: Basket) = withContext(Dispatchers.IO) {
        apiInterface.createBasket(basket).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    _basketLiveData.postValue(true)
                } else {
                    _errorBasketLiveData.postValue(response.message().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _errorBasketLiveData.postValue(t.message.toString())
            }

        })
    }


}