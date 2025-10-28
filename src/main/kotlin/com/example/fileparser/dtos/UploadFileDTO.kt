package com.example.fileparser.dtos

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class PersonRecord(
    val uuid: UUID,
    val id: String,
    val name: String,
    val likes: String,
    val transport: String,
    val avgSpeed: Double,
    val topSpeed: Double,
)

data class TransportData(
    val name: String,
    val transport: String,
    val topSpeed: Double,
)
