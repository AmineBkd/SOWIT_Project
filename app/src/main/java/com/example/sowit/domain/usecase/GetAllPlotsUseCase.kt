package com.example.sowit.domain.usecase

import com.example.sowit.data.repository.PlotRepository
import com.example.sowit.domain.model.Plot
import javax.inject.Inject

class GetAllPlotsUseCase @Inject constructor(
    private val repository: PlotRepository
) {
    suspend operator fun invoke(): List<Plot> {
        return repository.getAllPlots()
    }
}