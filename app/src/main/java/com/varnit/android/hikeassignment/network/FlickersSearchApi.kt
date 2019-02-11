package com.varnit.android.hikeassignment.network

import com.varnit.android.hikeassignment.data.images.FlickerPhotosResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url


interface FlickersSearchApi {
    // https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&text=tesla
    @GET fun getImages(@Url url: String) : Observable<FlickerPhotosResponseModel>
}