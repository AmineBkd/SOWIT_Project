package com.example.sowit.domain.repository

import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    suspend fun getLastKnownLocation(): Result<LatLng?>
}