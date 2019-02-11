package com.varnit.android.hikeassignment.domain.model

data class Resource<ResultType>(var status: Status, var data: ResultType? = null, var error: String? = null, var isPaginatedLoading: Boolean = false) {

    companion object {
        /**
         * Creates [Resource] with [Status.SUCCESS] and [data]
         */
        fun <ResultType> success(data: ResultType, isPaginatedLoading: Boolean = false): Resource<ResultType> = Resource(Status.SUCCESS, data, isPaginatedLoading = isPaginatedLoading)

        /**
         * Creates [Resource] with [Status.LOADING] to notify UI to load
         */
        fun <ResultType> loading(isPaginatedLoading: Boolean = false): Resource<ResultType> = Resource(Status.LOADING, isPaginatedLoading = isPaginatedLoading)

        /**
         * Creates [Resource] with [Status.ERROR] and passes the error object
         */
        fun <ResultType> error(networkCustomError: String?, isPaginatedLoading: Boolean = false): Resource<ResultType> = Resource(Status.ERROR, error = networkCustomError, isPaginatedLoading = isPaginatedLoading)

    }
}