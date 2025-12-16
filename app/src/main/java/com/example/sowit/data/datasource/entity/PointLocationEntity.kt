package com.example.sowit.data.datasource.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "point_location",
    foreignKeys = [
        ForeignKey(
            entity = PlotEntity::class,
            parentColumns = ["plotId"],
            childColumns = ["plotId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["plotId"])]
)
@Serializable
data class PointLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val plotId: Long,
    val latitude: Double,
    val longitude: Double,
)