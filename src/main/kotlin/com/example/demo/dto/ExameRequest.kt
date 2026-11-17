package com.example.demo.dto

/**
 * DTO (Data Transfer Object) de entrada do exame.
 *
 * Existe para o cliente nao precisar mandar o objeto Medico e o objeto Paciente
 * inteiros dentro do JSON. Ele manda so os ids, que e o que o INSERT nativo precisa.
 *
 * JSON esperado no POST/PUT:
 * {
 *   "tipo": "Hemograma",
 *   "resultado": null,
 *   "medicoId": 1,
 *   "pacienteId": 1
 * }
 */
data class ExameRequest(
    val tipo: String = "",
    val resultado: String? = null,
    val medicoId: Long = 0,
    val pacienteId: Long = 0
)
