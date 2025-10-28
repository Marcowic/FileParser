package com.example.fileparser.controllers

import com.example.fileparser.services.FileParserService
import org.springframework.http.MediaType
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
) {
    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(
        @RequestPart("file", required = true) file: MultipartFile,
    ): ResponseEntity<Any> {
        return fileParserService.processFile(file = file)
    }
}
