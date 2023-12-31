package com.daffamuhtar.fmkotlin.data.remote.response

import com.google.gson.annotations.SerializedName

data class RepairDetailProblemResponse(

    @SerializedName("problemId")
    var problemId: String,

    @SerializedName("problemDetail")
    val problemNote: String,

    @SerializedName("1")
    val problemPhoto1: String,

    @SerializedName("2")
    val problemPhoto2: String,

    @SerializedName("3")
    val problemPhoto3: String
)

