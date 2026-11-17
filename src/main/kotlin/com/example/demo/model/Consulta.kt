package com.example.demo.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "consulta")
open class Consulta(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    @Column(nullable = false)
    open var dataHora: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    open var especialidade: String = "",

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    open var paciente: Paciente? = null
)