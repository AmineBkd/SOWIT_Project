package com.example.sowit.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.sowit.domain.repository.LocationRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LocationRepository {
    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): Result<LatLng?> =
        suspendCoroutine { continuation ->
            val client = LocationServices.getFusedLocationProviderClient(context)
            client.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result?.let {
                        LatLng(it.latitude, it.longitude)
                    }
                    continuation.resume(Result.success(location))
                } else {
                    continuation.resume(
                        Result.failure(
                            task.exception ?: Exception("Unknown error")
                        )
                    )
                }
            }
        }
}