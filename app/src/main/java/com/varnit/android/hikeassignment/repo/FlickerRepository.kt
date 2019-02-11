package com.varnit.android.hikeassignment.repo

import android.arch.lifecycle.LiveData
import com.varnit.android.hikeassignment.data.images.FlickerPhotosResponseModel
import com.varnit.android.hikeassignment.domain.model.Resource

interface FlickerRepository {
    fun getImagesList(searchText: String?, pageNum: Int = 1) : LiveData<Resource<FlickerPhotosResponseModel>>
}