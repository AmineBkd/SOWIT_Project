package com.example.sowit.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import com.example.sowit.data.datasource.dao.PlotDao
import com.example.sowit.data.datasource.dao.PointLocationDao
import com.example.sowit.data.datasource.entity.PlotEntity
import com.example.sowit.data.datasource.entity.PointLocationEntity
import com.example.sowit.domain.model.Plot
import com.example.sowit.domain.model.PointLocation
import javax.inject.Inject

class PlotRepository @Inject constructor(
    private val plotDao: PlotDao,
    private val pointLocationDao: PointLocationDao
) {
    suspend fun insertEmptyPlot(name: String, color: Color): Plot {
        var plot = PlotEntity(name = name, color = color.toColorLong())
        plot = plot.copy(plotDao.insertPlot(plot))
        return Plot(
            id = plot.plotId,
            name = plot.name,
            color = color.toColorLong(),
            createdAt = System.currentTimeMillis()
        )
    }

    suspend fun insertPolygon(plotId: Long, points: List<PointLocation>) {
        pointLocationDao.insertPoints(points.map {
            PointLocationEntity(
                plotId = plotId,
                latitude = it.latitude,
                longitude = it.longitude
            )
        })
    }

    suspend fun getAllPlots(): List<Plot> {
        return plotDao.getAllPlots().map { plot ->
            Plot(
                id = plot.plotId,
                name = plot.name,
                color = plot.color,
                polygon = pointLocationDao.getPointsLocationByPlotId(plot.plotId)
                    .map { pointLocation ->
                        PointLocation(
                            id = pointLocation.id,
                            latitude = pointLocation.latitude,
                            longitude = pointLocation.longitude
                        )
                    }
            )
        }
    }

    suspend fun deletePlot(plot: Plot) {
        pointLocationDao.deletePoints(plot.id)
        plotDao.deletePlot(
            PlotEntity(
                plotId = plot.id,
                name = plot.name,
                color = plot.color,
                createdAt = plot.createdAt,
            )
        )
    }
}