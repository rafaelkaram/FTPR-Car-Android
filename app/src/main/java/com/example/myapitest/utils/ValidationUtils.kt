package com.example.myapitest.utils

object ValidationUtils {

    fun isValidYearFormat(year: String): Boolean {
        val yearPattern = Regex("^\\d{4}/\\d{4}$")
        if (!yearPattern.matches(year)) {
            return false
        }

        val parts = year.split("/")
        val year1 = parts[0].toIntOrNull()
        val year2 = parts[1].toIntOrNull()
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)

        return year1 != null && year2 != null &&
                year1 in 1900..(currentYear + 1) &&
                year2 in 1900..(currentYear + 1)
    }

    fun isValidLicencePlate(licence: String): Boolean {
        val cleanLicence = licence.trim().uppercase()

        // Formato antigo: ABC-1234
        val oldPattern = Regex("^[A-Z]{3}-\\d{4}$")
        // Formato Mercosul: ABC1D23
        val mercosulPattern = Regex("^[A-Z]{3}\\d[A-Z]\\d{2}$")

        return oldPattern.matches(cleanLicence) || mercosulPattern.matches(cleanLicence)
    }
}