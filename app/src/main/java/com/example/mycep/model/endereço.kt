package com.example.mycep.model

import java.io.Serializable

data class endereço (
    val logradouro: String,
    val cep: String,
    val nome: String,
    val img: Int,
) :Serializable