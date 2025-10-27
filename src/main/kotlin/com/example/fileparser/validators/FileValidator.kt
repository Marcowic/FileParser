package com.example.fileparser.validators

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class FileValidator {

    private val allowedFileTypes = listOf(
        ".txt", // add more if needed
    ).toSet()

    fun validateFileType(file: MultipartFile): Boolean {
        val fileName = file.originalFilename
        if (fileName == null) {
            return false
        }

        val finalDotIndex = fileName.lastIndexOf(".")
        val extension = fileName.substring(finalDotIndex, fileName.length)

        return allowedFileTypes.contains(extension)
    }
}
