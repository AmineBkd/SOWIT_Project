package com.example.sowit.ui.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sowit.ui.MapConstants
import com.example.sowit.ui.map.uiModel.ZoneClusterItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

private val bottomSheetPeekHeight = 40.dp

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            MapConstants.DEFAULT_LOCATION, MapConstants.DEFAULT_ZOOM
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        viewModel.getDeviceLocation()
    }

    LaunchedEffect(Unit) {
        viewModel.initState()
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        if (hasLocationPermission) {
            viewModel.getDeviceLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        MapContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            cameraPositionState = cameraPositionState,
            uiEvent = { viewModel.onEvent(it) },
            hasLocationPermission = hasLocationPermission
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapContent(
    modifier: Modifier = Modifier,
    state: MapState,
    cameraPositionState: CameraPositionState,
    uiEvent: (MapUiEvent) -> Unit,
    hasLocationPermission: Boolean,
) {
    val scope = rememberCoroutineScope()
    val mapProperties by remember(hasLocationPermission) {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = hasLocationPermission
            )
        )
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true, zoomControlsEnabled = false, compassEnabled = true
            )
        )
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
        )
    )

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = bottomSheetPeekHeight,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BottomSheetContent(zoneClusters = state.clusterItems, onItemClick = {
                    scope.launch {
                        val center = it.getCenterPosition()
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                    LatLng(center.latitude, center.longitude),
                                    MapConstants.PLOT_ZOOM
                                )
                            ), durationMs = MapConstants.CAMERA_ANIMATION_DURATION
                        )
                    }
                }, onDeleteItemClick = {
                    uiEvent(MapUiEvent.OnDeletePlotClicked(it))
                })
            }
        }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            MapMenu(
                canSavePlot = state.currentClusterItem.bounds.size >= ZoneClusterItem.MIN_POLYGON_POINTS,
                savePlotClick = {
                    uiEvent(MapUiEvent.OnSavePlotClicked(it))
                },
                clearPlotClick = {
                    uiEvent(MapUiEvent.OnClearPlotClicked)
                })
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng -> uiEvent(MapUiEvent.OnAddLocation(latLng)) },
                properties = mapProperties,
                uiSettings = uiSettings,
                onMapLoaded = {
                    scope.launch {
                        state.lastKnownLocation?.let {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.fromLatLngZoom(
                                        LatLng(it.latitude, it.longitude),
                                        MapConstants.USER_LOCATION_ZOOM
                                    )
                                ), durationMs = MapConstants.CAMERA_ANIMATION_DURATION
                            )
                        }
                    }
                }) {
                state.currentClusterItem.bounds.forEach {
                    Marker(
                        state = MarkerState(it),
                    )
                }

                if (state.currentClusterItem.bounds.size >= ZoneClusterItem.MIN_POLYGON_POINTS) {
                    Polygon(
                        points = state.currentClusterItem.bounds,
                        strokeColor = state.currentClusterItem.color,
                        fillColor = state.currentClusterItem.color.copy(alpha = 0.3f),
                        strokeWidth = 5f
                    )
                }

                state.clusterItems.forEach { zoneClusterItem ->
                    Polygon(
                        points = zoneClusterItem.bounds,
                        strokeColor = zoneClusterItem.color,
                        fillColor = zoneClusterItem.color.copy(alpha = 0.3f),
                        strokeWidth = 5f
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MapScreenPreview() {
    MapContent(
        state = MapState(
            lastKnownLocation = null,
            currentClusterItem = ZoneClusterItem(),
            clusterItems = listOf(),
        ),
        uiEvent = {},
        cameraPositionState = rememberCameraPositionState(),
        hasLocationPermission = false,
    )
}