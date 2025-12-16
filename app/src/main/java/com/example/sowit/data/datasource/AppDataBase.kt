package com.example.sowit.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sowit.data.datasource.dao.PlotDao
import com.example.sowit.data.datasource.dao.PointLocationDao
import com.example.sowit.data.datasource.entity.PlotEntity
import com.example.sowit.data.datasource.entity.PointLocationEntity

@Database(
    entities = [PlotEntity::class, PointLocationEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plotDao(): PlotDao
    abstract fun pointLocationDao(): PointLocationDao
}