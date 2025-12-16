package com.example.sowit.domain.usecase

import com.example.sowit.data.repository.PlotRepository
import com.example.sowit.domain.model.Plot
import javax.inject.Inject

class DeletePlotUseCase @Inject constructor(
    val repository: PlotRepository
) {
    suspend operator fun invoke(plot: Plot) {
        return repository.deletePlot(plot)
    }
}