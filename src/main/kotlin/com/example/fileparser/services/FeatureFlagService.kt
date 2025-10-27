package com.example.fileparser.services

import com.example.fileparser.utils.FeatureFlagKey
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class FeatureFlagService {

    private val flagMap: MutableMap<FeatureFlagKey, Boolean> = ConcurrentHashMap()

    init {
        FeatureFlagKey.values().forEach { flagMap[it] = false }
    }

    fun updateFlag(flag: FeatureFlagKey, status: Boolean): ResponseEntity<Any> {

        when {
            !flagMap.containsKey(flag) -> {
                return ResponseEntity.status(500).body(
                    mapOf("error" to "Error occurred trying to fetch the status of flag ${flag.simpleName}")
                )
            }

            flagMap[flag] != status -> {
                flagMap[flag] = status
                return ResponseEntity.ok(
                    mapOf(
                        "message" to "Updated flag ${flag.simpleName} to $status"
                    )
                )
            }

            flagMap[flag] == status -> {
                return ResponseEntity.ok(
                    mapOf(
                        "message" to "Flag ${flag.simpleName} already set to $status"
                    )
                )
            }

            else -> {
                return ResponseEntity.status(400).body(
                    mapOf(
                        "message" to "Flag ${flag.simpleName} already set to $status"
                    )
                )
            }
        }
    }

    fun getFlag(flag: FeatureFlagKey): ResponseEntity<Any> {
        return if (!flagMap.containsKey(flag))
            ResponseEntity.status(500).body(
                mapOf(
                    "error" to "Error occurred trying to fetch the status of flag ${flag.simpleName}"
                )
            )
        else {
            ResponseEntity.ok().body(
                mapOf(
                    "flag" to flag.simpleName,
                    "status" to flagMap[flag],
                )
            )
        }
    }
}
