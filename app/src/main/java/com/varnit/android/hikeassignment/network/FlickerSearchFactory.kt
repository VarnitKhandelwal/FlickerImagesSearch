package com.varnit.android.hikeassignment.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FlickerSearchFactory {
    companion object {
        val BASE_URL = "https://api.flickr.com/services/"

        fun create(): FlickersSearchApi {
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(FlickersSearchApi::class.java!!)
        }
    }
}