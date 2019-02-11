package com.varnit.android.hikeassignment.ui.images.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.varnit.android.hikeassignment.data.images.FlickerPhotosResponseModel
import com.varnit.android.hikeassignment.domain.model.Resource
import com.varnit.android.hikeassignment.repo.FlickerRepository

class FlickerPhotosViewModel(private val repository: FlickerRepository) : ViewModel() {
    private lateinit var flickerImagesObservable: LiveData<Resource<FlickerPhotosResponseModel>>

    private val autoCompleteSearch = MutableLiveData<SearchParams>()

    var pageNum = 1
    private var queryString: String = ""

    fun getImagesList(): LiveData<Resource<FlickerPhotosResponseModel>> {
        flickerImagesObservable = suggestionResults
        return flickerImagesObservable
    }

    val suggestionResults = Transformations.switchMap(autoCompleteSearch) {
        repository.getImagesList(it.query, it.page)
    }

    fun setAutoCompleteQuery(query: String?) {
        if (TextUtils.isEmpty(query)) {
            getImagesList()
        } else {
            queryString = query!!
            autoCompleteSearch.value = SearchParams(queryString, pageNum)
        }
    }

    fun loadNextPage() {
        autoCompleteSearch.value = SearchParams(queryString, ++pageNum)
    }

    inner class SearchParams(val query: String = "", val page: Int = 0)
}