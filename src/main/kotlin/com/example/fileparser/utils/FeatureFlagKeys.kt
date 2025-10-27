package com.example.fileparser.utils

enum class FeatureFlagKey(val simpleName: String) {
    SKIP_FILE_VALIDATION("skip-file-validation");

    companion object {
        private val map by lazy {
            values().associateBy { it.simpleName }
        }

        fun fromString(name: String): FeatureFlagKey? = map[name]
    }
}
