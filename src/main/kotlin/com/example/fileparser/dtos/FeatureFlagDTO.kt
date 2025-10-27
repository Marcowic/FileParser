package com.example.fileparser.dtos

import jakarta.validation.constraints.NotNull

data class UpdateFeatureFlagRequest(
    @field:NotNull
    val status: Boolean
)
