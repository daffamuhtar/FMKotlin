package com.daffamuhtar.fmkotlin.di.module

import android.content.Context
import com.daffamuhtar.fmkotlin.BuildConfig
import com.daffamuhtar.fmkotlin.app.Server
import com.daffamuhtar.fmkotlin.constants.ConstantsApp
import com.daffamuhtar.fmkotlin.data.api.RepairCheckApiHelper
import com.daffamuhtar.fmkotlin.data.api.RepairCheckApiHelperImpl
import com.daffamuhtar.fmkotlin.data.api.RepairOnNonperiodApiHelper
import com.daffamuhtar.fmkotlin.data.api.RepairOnNonperiodApiHelperImpl
import com.daffamuhtar.fmkotlin.util.NetworkHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//=======

val appModule = module {

    single { provideOkHttpClient() }

    single(named(ConstantsApp.BASE_URL_V1_0)) { provideRetrofitBlogV1(androidContext(), get()) }
    single(named(ConstantsApp.BASE_URL_V2_0)) { provideRetrofitBlogV2(androidContext(), get()) }
    single(named(ConstantsApp.BASE_URL_V2_0_REP)) { provideRetrofitBlogV2Rep(androidContext(), get()) }
    single(named(ConstantsApp.BASE_URL2)) { provideRetrofitInternalVendorV1(androidContext(), get()) }

    single { provideNetworkHelper(androidContext()) }

    single<RepairCheckApiHelper> {
        return@single RepairCheckApiHelperImpl()
    }

    single<RepairOnNonperiodApiHelper> {
        return@single RepairOnNonperiodApiHelperImpl()
    }
}


private fun provideNetworkHelper(context: Context) = NetworkHelper(context)

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer " + Server.token
                )
                .build()
            chain.proceed(newRequest)
        })
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()

private fun provideRetrofitBlogV1(
    context: Context,
    okHttpClient: OkHttpClient,
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Server.URL1)
        .client(okHttpClient)
        .build()

private fun provideRetrofitBlogV2(
    context: Context,
    okHttpClient: OkHttpClient,
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Server.URL1_V20)
        .client(okHttpClient)
        .build()

private fun provideRetrofitBlogV2Rep(
    context: Context,
    okHttpClient: OkHttpClient,
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Server.URL1_V20_REP)
        .client(okHttpClient)
        .build()

private fun provideRetrofitInternalVendorV1(
    context: Context,
    okHttpClient: OkHttpClient,
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Server.URL2)
        .client(okHttpClient)
        .build()

//private fun provideApiService(retrofit: Retrofit): RepairServices =
//    retrofit.create(RepairServices::class.java)
