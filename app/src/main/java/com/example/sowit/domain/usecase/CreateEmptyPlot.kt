package com.example.sowit.domain.usecase

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import com.example.sowit.data.repository.PlotRepository
import com.example.sowit.domain.model.Plot
import javax.inject.Inject

class CreateEmptyPlot @Inject constructor(
    private val repository: PlotRepository
) {
    suspend operator fun invoke(plot: Plot): Long {
        return repository.insertEmptyPlot(plot.name, Color.fromColorLong(plot.color)).id
    }
}