package com.example.sowit.domain.usecase

import com.example.sowit.data.repository.PlotRepository
import com.example.sowit.domain.model.Plot
import javax.inject.Inject

class CreatePlotUseCase @Inject constructor(
    private val repository: PlotRepository,
    private val createEmptyPlot: CreateEmptyPlot,
) {
    suspend operator fun invoke(plot: Plot): Plot {
        val savedPlot = Plot(
            id = 0, name = plot.name, color = plot.color, createdAt = System.currentTimeMillis()
        )
        val plotId = createEmptyPlot(savedPlot)

        repository.insertPolygon(
            plotId = plotId, points = plot.polygon
        )

        return savedPlot.copy(
            id = plotId, polygon = plot.polygon
        )
    }
}