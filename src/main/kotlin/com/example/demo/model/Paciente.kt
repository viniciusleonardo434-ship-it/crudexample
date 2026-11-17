package com.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "paciente")
open class Paciente(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    @Column(nullable = false)
    open var nome: String = "",

    @Column(nullable = false, unique = true)
    open var cpf: String = ""
)