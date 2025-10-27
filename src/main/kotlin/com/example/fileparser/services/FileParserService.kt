package com.example.fileparser.services

import com.example.fileparser.utils.FeatureFlagKey
import com.example.fileparser.validators.FileValidator
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 * Service to parse and process user uploaded files/documents.
 */
@Service
class FileParserService(
    private val fileValidator: FileValidator,
    private val featureFlagService: FeatureFlagService
) {
    fun processFile(file: MultipartFile): ResponseEntity<Any> {
        // have business logic here
        // add feature flag check
        val isFileValid = if (!featureFlagService.isFlagEnabled(flag = FeatureFlagKey.SKIP_FILE_VALIDATION)) {
            fileValidator.validateFileType(file = file)
        } else {
            true
        }

        if (isFileValid) {
            val output = parseFileContents(file = file)

            return ResponseEntity.ok(
                mapOf(
                    "message" to "successfully processed file=$",
                    "output" to "THIS SHOULD CONTAIN THE RESULT"
                )
            )
        } else {
            return ResponseEntity.badRequest().body(
                mapOf(
                    "error" to "File is invalid"
                )
            )
        }
    }

    private fun parseFileContents(file: MultipartFile) {
        // iterate over the lines and apply row validation against the specified structure of the data
    }
}
