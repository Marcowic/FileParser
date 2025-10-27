package com.example.fileparser.services

import com.example.fileparser.utils.FeatureFlagKey
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeatureFlagServiceTest {

    private val featureFlagService = FeatureFlagService()

    @ParameterizedTest
    @CsvSource(
        "skip-file-validation, true",
        "skip-file-validation, True",
        "skip-file-validation, TRUE",
    )
    fun `update valid flag to true`(flagString: String, newValue: Boolean) {
        val flag = FeatureFlagKey.fromString(flagString)!!

        // Update flag
        val response: ResponseEntity<Any> = featureFlagService.updateFlag(flag = flag, status = newValue)

        assertEquals(expected = HttpStatus.OK, actual = response.statusCode)

        val body = response.body

        assertNotNull(body)

        val message = (body as Map<*, *>)["message"]
        assertEquals(expected = "Updated flag skip-file-validation to true", actual = message)
    }

    @ParameterizedTest
    @CsvSource(
        "skip-file-validation, false, false",
        "skip-file-validation, False, false",
        "skip-file-validation, FALSE, false",
    )
    fun `update valid flag to false`(flagString: String, newValue: Boolean) {
        val flag = FeatureFlagKey.fromString(flagString)!!

        // Update flag to true initially
        featureFlagService.updateFlag(flag = flag, status = true)

        // Update to false
        val response: ResponseEntity<Any> = featureFlagService.updateFlag(flag = flag, status = newValue)

        assertEquals(expected = HttpStatus.OK, actual = response.statusCode)

        val body = response.body

        assertNotNull(body)

        val message = (body as Map<*, *>)["message"]
        assertEquals(expected = "Updated flag skip-file-validation to false", actual = message)
    }

    @ParameterizedTest
    @CsvSource(
        "skip-file-validation, true",
        "skip-file-validation, false",
    )
    fun `get skip-file-validation flag status`(flag: String, expected: Boolean) {
        val flag = FeatureFlagKey.fromString("skip-file-validation")!!

        // update flag
        featureFlagService.updateFlag(flag = flag, status = expected)

        val response = featureFlagService.getFlag(flag)

        assertEquals(expected = HttpStatus.OK, actual = response.statusCode)

        val body = response.body

        assertNotNull(body)

        val status = (body as Map<*, *>)["status"]
        assertEquals(expected = expected, actual = status)
    }

    @Test
    fun `isFlagEnabled should return the default value false for any valid flag`() {
        val status = featureFlagService.isFlagEnabled(FeatureFlagKey.fromString("skip-file-validation")!!)

        assertFalse(status)
    }

    @Test
    fun `isFlagEnabled should return the value true when enabled`() {
        val flag = FeatureFlagKey.fromString("skip-file-validation")!!

        featureFlagService.updateFlag(flag = flag, status = true)

        val status = featureFlagService.isFlagEnabled(flag)

        assertTrue(status)
    }

}
