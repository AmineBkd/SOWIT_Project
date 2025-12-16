package com.example.sowit.data.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sowit.data.datasource.entity.PlotEntity

@Dao
interface PlotDao {
    @Insert
    suspend fun insertPlot(plotEntity: PlotEntity): Long

    @Delete
    suspend fun deletePlot(plotEntity: PlotEntity)

    @Query("SELECT * FROM plot WHERE plotId = :plotId")
    suspend fun getPlotById(plotId: Long): PlotEntity?

    @Query("SELECT * FROM plot")
    suspend fun getAllPlots(): List<PlotEntity>
}