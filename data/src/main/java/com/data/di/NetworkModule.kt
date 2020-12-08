package com.data.di

import com.data.apiservice.ApiEndPoints.Companion.BASE_URL
import com.data.apiservice.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*

@Module
class NetworkModule {

    @Provides
    fun bindApiService(retrofit: Retrofit): ApiService? {
        return retrofit.create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideGson(): Gson? {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Singleton
    @Provides
    fun provideSSLSocketFactory(): SSLSocketFactory? {
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, provideTrustManager(), SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        return sslContext!!.socketFactory
    }

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient? {
        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(
            provideSSLSocketFactory()!!,
            (provideTrustManager()[0] as X509TrustManager?)!!
        )
        builder.hostnameVerifier(HostnameVerifier { hostName: String?, session: SSLSession? -> true })
        builder.connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        builder.addInterceptor(provideHttpLoggingInterceptor()!!)
        return builder.build()
    }

//    @Provides
//    fun provideHttpCache(): Cache? {
//        val cacheSize = 10 * 1024 * 1024
//        return Cache(context.getCacheDir(), cacheSize.toLong())
//    }
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(provideOkHttpClientBuilder())
            .build()
    }


    @Provides
    fun provideTrustManager(): Array<TrustManager?> {
        return arrayOf(
            object : X509TrustManager {
                @Suppress("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Suppress("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
    }

}