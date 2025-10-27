package com.example.fileparser.controllers

import com.example.fileparser.services.FileParserService
import com.example.fileparser.validators.FileValidator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * Controller for uploading files.
 */
@RestController
@RequestMapping("/api/v1/files")
class FilesController(
    private val fileParserService: FileParserService,
    private val fileValidator: FileValidator,
) {
    @PostMapping("/upload", consumes = ["multipart/form-data"])
    fun uploadFile(
        @RequestPart("file", required = true) file: MultipartFile,
    ): ResponseEntity<Any> {
        return fileParserService.processFile(file = file)
    }
}
