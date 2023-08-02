package com.daffamuhtar.fmkotlin.ui.check

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.VolleyLog
import com.daffamuhtar.fmkotlin.app.ApiConfig
import com.daffamuhtar.fmkotlin.model.response.RepairCheckResponse
import com.daffamuhtar.fmkotlin.model.response.ErrorResponse
import com.daffamuhtar.fmkotlin.services.RepairServices
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class RepairCheckViewModel : ViewModel() {

    private val _isLoadingGetAllCheckRepair = MutableLiveData<Boolean>()
    val isLoadingGetAllCheckRepair: LiveData<Boolean> = _isLoadingGetAllCheckRepair

    private val _isSuccessGetAllCheckRepair = MutableLiveData<Boolean>()
    val isSuccessGetAllCheckRepair: LiveData<Boolean> = _isSuccessGetAllCheckRepair

    private val _messageGetAllCheckRepair = MutableLiveData<String>()
    val messageGetAllCheckRepair: LiveData<String> = _messageGetAllCheckRepair

    private val _repairList = MutableLiveData<List<RepairCheckResponse>>()
    val repairList: LiveData<List<RepairCheckResponse>> = _repairList

    fun getAllCheckRepair(
        context: Context,
        apiVersion: String,
        userId: String,
    ) {
        _isLoadingGetAllCheckRepair.value = true

        val retrofit = ApiConfig.getRetrofit(context, apiVersion)
        val services = retrofit?.create(RepairServices::class.java)
        val client = services?.getCheckRepair(userId, 0)

        client?.enqueue(object : Callback<List<RepairCheckResponse>> {
            override fun onResponse(
                call: Call<List<RepairCheckResponse>>,
                response: Response<List<RepairCheckResponse>>
            ) {
                _isLoadingGetAllCheckRepair.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.w(
                        "RESULT:",
                        GsonBuilder().setPrettyPrinting().create().toJson(response.body())
                    )

                    val repair: List<RepairCheckResponse>? = response.body()
                    if (responseBody != null) {
                        _repairList.value = repair!!
                    }

                } else {
                    val responseErrorBody = response.errorBody()
                    if (responseErrorBody != null) {
                        Log.w(
                            "RESULT:",
                            "onResponse: Not Success " + response.code() + GsonBuilder().setPrettyPrinting()
                                .create().toJson(responseErrorBody)
                        )
                        val converter: Converter<ResponseBody?, ErrorResponse> =
                            retrofit.responseBodyConverter(
                                ErrorResponse::class.java,
                                arrayOfNulls<Annotation>(0)
                            )
                        var errorModel: ErrorResponse? = null
                        try {
                            errorModel = converter.convert(responseErrorBody)
                            val status: Boolean = errorModel?.status ?: false
                            val message: String = errorModel?.message ?: "no message"

                            _isSuccessGetAllCheckRepair.value = status
                            _messageGetAllCheckRepair.value = message

                            Toast.makeText(context, "Gagal Mengirim $message", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Log.d(VolleyLog.TAG, "onResponse: $e")
                        }
                    }

                }

            }

            override fun onFailure(call: Call<List<RepairCheckResponse>>, t: Throwable) {
                _isLoadingGetAllCheckRepair.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                _messageGetAllCheckRepair.value =
                    "Gagal terhubung ke server, periksa koneksi Anda dan coba lagi nanti."
            }
        })
    }

}