package com.example.sowit.ui.map.uiModel

import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil

data class ZoneClusterItem(
    val id: Long? = null,
    val title: String = "",
    val bounds: List<LatLng> = listOf(),
    val color: Color = Color.Green,
) {
    companion object {
        const val MIN_POLYGON_POINTS = 3
    }

    fun getCenterPosition(): LatLng {
        return if (bounds.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.builder()
            bounds.forEach { point -> boundsBuilder.include(point) }
            boundsBuilder.build().center
        } else {
            LatLng(0.0, 0.0)
        }
    }

    fun getArea(): Double {
        return if (bounds.size >= MIN_POLYGON_POINTS) {
            SphericalUtil.computeArea(bounds)
        } else { 0.0 }
    }
}