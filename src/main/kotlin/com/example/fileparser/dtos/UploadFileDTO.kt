package com.example.fileparser.dtos

import jakarta.validation.constraints.NotBlank
import java.io.File

data class UploadFileRequest(
    @field:NotBlank(message = "Must provide a file to upload")
    val file: File
)