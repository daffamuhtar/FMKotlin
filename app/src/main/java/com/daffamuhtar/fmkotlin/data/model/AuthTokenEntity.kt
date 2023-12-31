package com.daffamuhtar.fmkotlin.data.model

data class AuthTokenEntity(
    val accessToken: String,
    val refreshToken: String,
    // the rest of the model according to the JSON
)