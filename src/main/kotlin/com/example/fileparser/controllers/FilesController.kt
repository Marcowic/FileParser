package com.example.fileparser.controllers

import com.example.fileparser.dtos.UploadFileRequest
import com.example.fileparser.services.FileParserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for uploading files.
 */
@RestController
@RequestMapping("/api/v1/files")
class FilesController(
    private val fileParserService: FileParserService,
) {
    @PostMapping
    fun uploadFile(
        @Valid @RequestBody request: UploadFileRequest
    ): ResponseEntity<Any> {
        return fileParserService.process(request)
    }

}