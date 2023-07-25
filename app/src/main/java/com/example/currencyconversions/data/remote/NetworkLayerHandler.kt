package com.example.currencyconversions.data.remote

import com.example.currencyconversions.data.DataStateWrapper
import com.example.currencyconversions.domain.entity.response.BaseResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

suspend fun <T : BaseResponse> apiCallHandler(
    dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> T
): DataStateWrapper<T> {
    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()
            if (!(response as BaseResponse).success)
                throw Exception(Gson().toJson(response))
            DataStateWrapper.Success(response)
        } catch (throwable: Throwable) {
            handleError(throwable)
        }
    }
}

fun <T> handleError(throwable: Throwable): DataStateWrapper<T> {
    val errorString = (throwable as? HttpException)?.response()?.errorBody()?.string()
    return try {
        val errorResponse =
            Gson().fromJson(errorString ?: throwable.message, BaseResponse::class.java)
        DataStateWrapper.Error(errorResponse.error?.code, errorResponse.error?.info!!)
    } catch (e: Exception) {
        DataStateWrapper.Error(message = "Network Error: There was an error connecting. Please check your internet.")
    }
}
