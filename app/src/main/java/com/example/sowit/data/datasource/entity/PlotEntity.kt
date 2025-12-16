package com.example.sowit.data.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plot")
data class PlotEntity(
    @PrimaryKey(autoGenerate = true)
    val plotId: Long = 0,
    val name: String,
    val color: Long,
    val createdAt: Long = System.currentTimeMillis()
)