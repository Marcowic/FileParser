package com.example.fileparser.services

import com.example.fileparser.dtos.UploadFileRequest
import com.example.fileparser.validators.FileContentValidator
import com.example.fileparser.validators.FileTypeValidator
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Service to parse and process user uploaded files/documents.
 */
@Service
class FileParserService(
    val fileContentValidator: FileContentValidator,
    val fileTypeValidator: FileTypeValidator,
) {

    fun process(request: UploadFileRequest): ResponseEntity<Any> {
        // have business logic here
        // add feature flag check
        // add validation

        return ResponseEntity.ok(
            mapOf(
                "message" to "successfully processed file=${request.file}",
                "output" to "THIS SHOULD CONTAIN THE RESULT"
            )
        )
    }
}