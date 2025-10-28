package com.example.fileparser.services

import com.example.fileparser.dtos.PersonRecord
import com.example.fileparser.dtos.TransportData
import com.example.fileparser.utils.FeatureFlagKey
import com.example.fileparser.validators.FileValidator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.IllegalArgumentException
import java.util.UUID

/**
 * Service to parse and process user uploaded files/documents.
 */
@Service
class FileParserService(
    private val fileValidator: FileValidator, private val featureFlagService: FeatureFlagService
) {

    private val objectMapper = jacksonObjectMapper()

    fun processFile(file: MultipartFile): ResponseEntity<Any> {

        val isFileValid = if (!featureFlagService.isFlagEnabled(flag = FeatureFlagKey.SKIP_FILE_VALIDATION)) {
            fileValidator.validateFileType(file = file)
        } else {
            true
        }

        if (isFileValid) {
            try {
                val jsonBytes = parseFileContents(file = file)
                val resource = ByteArrayResource(jsonBytes)

                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"")
                    .contentType(MediaType.APPLICATION_JSON).body(resource)

            } catch (ex: Exception) {
                return ResponseEntity.badRequest().body(
                    mapOf(
                        "error" to "Malformed content in provided resource, $ex"

                    )
                )
            }
        } else {
            return ResponseEntity.badRequest().body(
                mapOf(
                    "error" to "File is invalid"
                )
            )
        }
    }

    private fun parseFileContents(file: MultipartFile): ByteArray {
        val inputStream = file.inputStream

        val lines = inputStream.bufferedReader().use { reader -> reader.readLines() }

        val result = mutableListOf<Any>()

        lines.forEach {
            val data = it.split("|")
            if (!featureFlagService.isFlagEnabled(flag = FeatureFlagKey.SKIP_FILE_VALIDATION)) {
                val person = parsePersonRecordFromString(data = data)

                result.add(
                    TransportData(
                        name = person.name,
                        transport = person.transport,
                        topSpeed = person.topSpeed,
                    )
                )
            } else {
                result.add(
                    mapOf(
                        "name" to data[2], "transport" to data[4], "topSpeed" to data[6]
                    )
                )
            }
        }

        return objectMapper.writeValueAsBytes(result)
    }

    private fun parsePersonRecordFromString(data: List<String>): PersonRecord {
        if (data.size != 7) {
            throw InvalidRecordFormatException("Malformed record in file, size=${data.size}")
        }

        return PersonRecord(
            uuid = UUID.fromString(data[0]),
            id = data[1],
            name = data[2],
            likes = data[3],
            transport = data[4],
            avgSpeed = data[5].toDouble(),
            topSpeed = extractValueFromString(data[6]).toDouble(),
        )
    }

    private fun extractValueFromString(s: String): String {
        var i = s.length - 1

        while (i >= 0 && !s[i].isDigit()) i--
        return s.take(i)
    }
}

class InvalidRecordFormatException(message: String) : IllegalArgumentException(message)
