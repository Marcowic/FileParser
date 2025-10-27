package com.example.fileparser.controllers

import com.example.fileparser.dtos.UpdateFeatureFlagRequest
import com.example.fileparser.services.FeatureFlagService
import com.example.fileparser.utils.FeatureFlagKey
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/flags")
class FeatureFlagController(
    private val featureFlagService: FeatureFlagService,
) {

    @PatchMapping("/{flagToUpdate}")
    fun updateFlag(
        @PathVariable flagToUpdate: String,
        @Valid @RequestBody body: UpdateFeatureFlagRequest,
    ): ResponseEntity<Any> {

        val flag = FeatureFlagKey.fromString(name = flagToUpdate)
            ?: return ResponseEntity.status(400)
                .body(mapOf("error" to "Invalid feature flag: $flagToUpdate"))

        val newStatus = body.status

        return featureFlagService.updateFlag(
            flag = flag,
            status = newStatus,
        )
    }

// Does this make sense to add ?
//    @PostMapping
//    fun addFlag() {}

    @GetMapping("/{flagToCheck}")
    fun getFlagByID(
        @PathVariable flagToCheck: String
    ): ResponseEntity<Any> {
        val flag = FeatureFlagKey.fromString(name = flagToCheck)
            ?: return ResponseEntity.status(400)
                .body(mapOf("error" to "Invalid feature flag: $flagToCheck"))

        return featureFlagService.getFlag(flag = flag)
    }
}
