package com.example.sowit.ui.map

import com.example.sowit.ui.map.uiModel.ZoneClusterItem
import com.google.android.gms.maps.model.LatLng

sealed class MapUiEvent {
    data class OnSavePlotClicked(val plotName: String) : MapUiEvent()
    data class OnDeletePlotClicked(val zoneCluster: ZoneClusterItem) : MapUiEvent()
    data class OnAddLocation(val latLng: LatLng) : MapUiEvent()
    object OnClearPlotClicked : MapUiEvent()
}