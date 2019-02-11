package com.varnit.android.hikeassignment.repo

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.ConnectivityManager
import com.varnit.android.hikeassignment.application.FlickerImagesApplication
import com.varnit.android.hikeassignment.data.images.FlickerPhotosResponseModel
import com.varnit.android.hikeassignment.domain.model.Resource
import com.varnit.android.hikeassignment.network.FlickerSearchFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer



class FlickerRepositoryRemoteDataSource(private val appContext: Context) {

    companion object {
        private const val ERROR_CODE_NETWORK_DISCONNECTED = "600"
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: FlickerRepositoryRemoteDataSource? = null

        fun getInstance(appContext: Application): FlickerRepositoryRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = FlickerRepositoryRemoteDataSource(appContext)
            }
            return INSTANCE!!
        }
    }

    fun getImagesList(searchText: String? = "tesla", pageNum: Int): LiveData<Resource<FlickerPhotosResponseModel>> {
        val data = MutableLiveData<Resource<FlickerPhotosResponseModel>>()
        val url = FlickerSearchFactory.BASE_URL + "rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&text=" + searchText + "&page=" + pageNum

        val flickerImagesApplication = FlickerImagesApplication.create(appContext)
        val flickerService = flickerImagesApplication.getFlickerService()
        if(isNetworkAvailable()) {
            flickerService?.getImages(url)?.
                    subscribeOn(flickerImagesApplication.subscribeScheduler())?.
                    observeOn(AndroidSchedulers.mainThread())?.
                    subscribe(object : Consumer<FlickerPhotosResponseModel> {
                override fun accept(response: FlickerPhotosResponseModel) {
                    data.value = Resource.success(response)
                }
            }, object : Consumer<Throwable> {
                override fun accept(errorResponse: Throwable?) {
                    data.value = Resource.error(errorResponse?.message)
                }
            })
        } else {
            data.value = Resource.error(getNetworkError())
        }
        return data
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getNetworkError(): String {
        return "No Internet Connection"
    }
}