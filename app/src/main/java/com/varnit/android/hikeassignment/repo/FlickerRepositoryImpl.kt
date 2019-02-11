package com.varnit.android.hikeassignment.repo

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.content.Context
import com.varnit.android.hikeassignment.data.images.FlickerPhotosResponseModel
import com.varnit.android.hikeassignment.domain.model.Resource
import com.varnit.android.hikeassignment.domain.model.Status

class FlickerRepositoryImpl(private val appContext: Context,
                            private val remoteDataSource: FlickerRepositoryRemoteDataSource) : FlickerRepository {
    companion object {
        const val AP_OK = "ok"
        const val API_SUCCESS = "success"
        const val NETWORK_ERROR = "Network Error"
        const val PARSING_ERROR = "Parsing Error"

        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: FlickerRepositoryImpl? = null

        fun getInstance(appContext: Context, remoteDataSource: FlickerRepositoryRemoteDataSource): FlickerRepository {
            if (INSTANCE == null) {
                INSTANCE = FlickerRepositoryImpl(appContext, remoteDataSource)
            }
            return INSTANCE!!
        }
    }

    override fun getImagesList(searchText: String?, pageNum: Int): LiveData<Resource<FlickerPhotosResponseModel>> {
        val remoteLiveData = remoteDataSource.getImagesList(searchText, pageNum)
        return Transformations.map(remoteLiveData) {

            when (it.status) {
                Status.SUCCESS -> {
                    val flickerImagesAddressResponse = it.data
                    if (flickerImagesAddressResponse != null && isSuccessResponse(flickerImagesAddressResponse.status)) {
                        val savedAddressData = flickerImagesAddressResponse
                        if (savedAddressData != null)
                            Resource.success(savedAddressData, it.isPaginatedLoading)
                        else {
//                            getVolleyParsingError(it.isPaginatedLoading)
                            Resource.error(getNetworkError())
                        }
                    } else {
                        Resource.error(NETWORK_ERROR, it.isPaginatedLoading)
                    }
                }
                Status.ERROR -> {
                    Resource.error(it.error, it.isPaginatedLoading)
                }
                Status.LOADING -> {
                    Resource.loading(it.isPaginatedLoading)
                }
            }
        }
    }

    private fun isSuccessResponse(status: String?): Boolean = AP_OK.equals(status, true)

    private fun getNetworkError(): String {
        return "No Internet Connection"
    }
}