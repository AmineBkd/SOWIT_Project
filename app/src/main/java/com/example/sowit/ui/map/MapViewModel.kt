package com.example.sowit.ui.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.graphics.toColorLong
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sowit.data.repository.LocationRepositoryImpl
import com.example.sowit.domain.model.Plot
import com.example.sowit.domain.model.PointLocation
import com.example.sowit.domain.usecase.CreatePlotUseCase
import com.example.sowit.domain.usecase.DeletePlotUseCase
import com.example.sowit.domain.usecase.GetAllPlotsUseCase
import com.example.sowit.ui.map.uiModel.ZoneClusterItem
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepositoryImpl,
    private val createPlotUseCase: CreatePlotUseCase,
    private val getAllPlotsUseCase: GetAllPlotsUseCase,
    private val deletePlotUseCase: DeletePlotUseCase,
) : ViewModel() {
    private var _state = mutableStateOf(MapState())
    val state: State<MapState> = _state

    fun initState() {
        viewModelScope.launch {
            updateState(
                state.value.copy(
                    currentClusterItem = ZoneClusterItem(), clusterItems = fetchZoneClusters()
                )
            )
        }
    }

    fun getDeviceLocation() {
        try {
            viewModelScope.launch {
                locationRepository.getLastKnownLocation().onSuccess { location ->
                    location?.let {
                        updateState(state.value.copy(lastKnownLocation = it))
                    }
                }.onFailure { e ->
                    Timber.e("Location error: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Timber.e(message = "Retrieve CurrentLocation Error: %s", e.message)
        }
    }

    fun onEvent(event: MapUiEvent) {
        when (event) {
            is MapUiEvent.OnSavePlotClicked -> {
                saveSelectedPlot(event.plotName)
            }

            MapUiEvent.OnClearPlotClicked -> {
                clearSelectedPlot()
            }

            is MapUiEvent.OnAddLocation -> {
                updateLocationClusterState(event.latLng)
            }

            is MapUiEvent.OnDeletePlotClicked -> {
                deletePlot(event.zoneCluster)
            }
        }
    }

    private fun saveSelectedPlot(plotName: String) {
        viewModelScope.launch {
            val currentPlot = createPlotUseCase(
                Plot(
                    0,
                    name = plotName,
                    color = state.value.currentClusterItem.color.toColorLong(),
                    polygon = state.value.currentClusterItem.bounds.map {
                        PointLocation(
                            0, it.latitude, it.longitude
                        )
                    })
            )

            updateState(
                state.value.copy(
                    currentClusterItem = ZoneClusterItem(
                        currentPlot.id,
                        currentPlot.name,
                        color = Color.fromColorLong(currentPlot.color),
                    ), clusterItems = fetchZoneClusters()
                )
            )
        }
    }

    private suspend fun fetchZoneClusters(): List<ZoneClusterItem> =
        getAllPlotsUseCase().map { plot ->
            val zoneCluster = ZoneClusterItem(
                id = plot.id,
                title = plot.name,
                color = Color.fromColorLong(plot.color),
                bounds = plot.polygon.map { LatLng(it.latitude, it.longitude) })
            zoneCluster
        }

    private fun deletePlot(zoneClusterItem: ZoneClusterItem) {
        viewModelScope.launch {
            zoneClusterItem.id?.let { id ->
                try {
                    deletePlotUseCase(
                        Plot(
                            id,
                            zoneClusterItem.title,
                            zoneClusterItem.color.toColorLong(),
                            zoneClusterItem.bounds.map { PointLocation(0, it.latitude, it.longitude) },
                        )
                    )
                    val updatedClusters = fetchZoneClusters()
                    updateState(state.value.copy(clusterItems = updatedClusters))
                } catch (e: Exception) {
                    Timber.e("Delete failed: ${e.message}")
                }
            }
        }
    }

    private fun updateLocationClusterState(location: LatLng) {
        val currentClusterBounds = state.value.currentClusterItem.bounds.toMutableList()
        currentClusterBounds.add(location)
        updateState(
            state.value.copy(
                currentClusterItem = state.value.currentClusterItem.copy(
                    bounds = currentClusterBounds
                )
            )
        )
    }

    private fun clearSelectedPlot() {
        updateState(
            state.value.copy(
                currentClusterItem = ZoneClusterItem()
            )
        )
    }

    private fun updateState(value: MapState) {
        _state.value = value
    }
}