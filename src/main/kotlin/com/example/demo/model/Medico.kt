package com.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "medico")
open class Medico(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    @Column(nullable = false)
    open var nome: String = "",

    @Column(nullable = false, unique = true, length = 20)
    open var crm: String = "",

    @Column(nullable = false)
    open var especialidade: String = "",

    @Column(nullable = false)
    open var ativo: Boolean = true
)
