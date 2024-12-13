package com.kitchingapp.domain.entities

import java.time.LocalDate

data class User(
    val name: String,
    val birth: LocalDate,
    val email: String,
    val healthCertificate: LocalDate
)
