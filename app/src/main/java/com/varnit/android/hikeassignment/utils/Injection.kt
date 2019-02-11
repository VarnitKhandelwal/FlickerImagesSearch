package com.varnit.android.hikeassignment.utils

import android.app.Application
import com.varnit.android.hikeassignment.repo.FlickerRepository
import com.varnit.android.hikeassignment.repo.FlickerRepositoryImpl
import com.varnit.android.hikeassignment.repo.FlickerRepositoryRemoteDataSource

object FlickerImagesInjection {

    fun provideRepository(app: Application): FlickerRepository {
        val channelsRemoteDataSource = provideFlickerRepoRemoteDataSource(app)
        return FlickerRepositoryImpl.getInstance(app, channelsRemoteDataSource)
    }

    private fun provideFlickerRepoRemoteDataSource(app: Application): FlickerRepositoryRemoteDataSource = FlickerRepositoryRemoteDataSource.getInstance(app)
}