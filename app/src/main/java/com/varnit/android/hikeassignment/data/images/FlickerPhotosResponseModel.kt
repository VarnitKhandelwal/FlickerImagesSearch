package com.varnit.android.hikeassignment.data.images

import com.google.gson.annotations.SerializedName

data class FlickerPhotosResponseModel(
    @SerializedName("photos")
    var photos: Photos?,
    @SerializedName("stat")
    var status: String?
) {
    data class Photos(
        @SerializedName("page")
        var page: Int?,
        @SerializedName("pages")
        var pages: Int?,
        @SerializedName("perpage")
        var perpage: Int?,
        @SerializedName("photo")
        var photo: ArrayList<Photo?>?,
        @SerializedName("total")
        var total: String?
    ) {
        data class Photo(
            @SerializedName("farm")
            var farm: Int?,
            @SerializedName("id")
            var id: String?,
            @SerializedName("isfamily")
            var isfamily: Int?,
            @SerializedName("isfriend")
            var isfriend: Int?,
            @SerializedName("ispublic")
            var ispublic: Int?,
            @SerializedName("owner")
            var owner: String?,
            @SerializedName("secret")
            var secret: String?,
            @SerializedName("server")
            var server: String?,
            @SerializedName("title")
            var title: String?
        )
    }
}