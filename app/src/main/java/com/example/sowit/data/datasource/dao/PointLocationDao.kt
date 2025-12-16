package com.example.sowit.data.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sowit.data.datasource.entity.PointLocationEntity

@Dao
interface PointLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoints(pointLocationEntity: List<PointLocationEntity>)

    @Query("DELETE FROM point_location WHERE plotId = :plotId")
    suspend fun deletePoints(plotId: Long)

    @Query("SELECT * FROM point_location WHERE plotId = :plotId")
    suspend fun getPointsLocationByPlotId(plotId: Long): List<PointLocationEntity>
}