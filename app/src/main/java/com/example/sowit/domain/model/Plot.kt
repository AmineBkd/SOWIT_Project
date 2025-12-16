package com.example.sowit.domain.model

data class Plot(
    val id: Long,
    val name: String,
    val color: Long,
    val polygon: List<PointLocation> = listOf(),
    val createdAt: Long = System.currentTimeMillis()
)