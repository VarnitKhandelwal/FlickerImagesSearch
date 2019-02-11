package com.varnit.android.hikeassignment.application

import android.app.Application
import android.content.Context
import com.varnit.android.hikeassignment.network.FlickerSearchFactory
import com.varnit.android.hikeassignment.network.FlickersSearchApi
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class FlickerImagesApplication : Application() {
    private var flickersSearchApi: FlickersSearchApi? = null
    private var scheduler: Scheduler? = null

    companion object {
        fun create(context: Context): FlickerImagesApplication {
            return FlickerImagesApplication.get(context)
        }

        private fun get(context: Context): FlickerImagesApplication {
            return context.getApplicationContext() as FlickerImagesApplication
        }
    }

    fun getFlickerService(): FlickersSearchApi? {
        if (flickersSearchApi == null) {
            flickersSearchApi = FlickerSearchFactory.create()
        }

        return flickersSearchApi
    }

    fun subscribeScheduler(): Scheduler? {
        if (scheduler == null) {
            scheduler = Schedulers.io()
        }

        return scheduler
    }
}