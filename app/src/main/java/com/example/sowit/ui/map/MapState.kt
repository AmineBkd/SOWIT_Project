package com.example.sowit.ui.map

import com.example.sowit.ui.map.uiModel.ZoneClusterItem
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val lastKnownLocation: LatLng? = null,
    val currentClusterItem: ZoneClusterItem = ZoneClusterItem(),
    val clusterItems: List<ZoneClusterItem> = listOf(),
)