package com.example.currency_bank_misr.data.di

import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.APP_VERSION
import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.DEFAULT_LANGUAGE
import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.HEADER_ACCEPT
import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.HEADER_AUTHORIZATION
import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.HEADER_CONTENT_TYPE
import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.JSON
import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.MOBILE
import com.example.currency_bank_misr.data.di.ConstantsUtils.Companion.PLATFORM
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = ConstantsUtils.BASE_URL

    @Provides
    @Singleton
    fun getRetrofitInstance(BASE_URL: String): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)


        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


    }
        private fun getHttpClient(
            interceptor: Interceptor,
            isDebug: Boolean
        ) = getOkHttpClient()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(getHttpLoggingInterceptor(isDebug))
            .dispatcher(Dispatcher().also { it.maxRequests = 1 })
            .followSslRedirects(true)
            .followRedirects(true)
            .build()

        private fun getHttpLoggingInterceptor(isDebug: Boolean): Interceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            return interceptor
        }

        private fun getOkHttpClient(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
        }

    class HeaderInterceptor(
        private val versionName: String,
        private val tokenProvider: () -> String?,
        private val languageProvider: () -> Locale,
        private val currentLang:String
    ) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val builder = request.newBuilder()

            builder
                .header(HEADER_ACCEPT, JSON)
                .addHeader(DEFAULT_LANGUAGE, currentLang)
                .header(HEADER_CONTENT_TYPE, JSON)
                .header(PLATFORM, MOBILE)
                .header(APP_VERSION, versionName)

            tokenProvider()?.let {
                builder.header(HEADER_AUTHORIZATION, it)
            }

            return chain.proceed(builder.build())
        }
    }
}