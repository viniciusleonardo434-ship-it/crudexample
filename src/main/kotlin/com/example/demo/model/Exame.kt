package com.example.demo.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "exame")
open class Exame(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    @Column(nullable = false)
    open var tipo: String = "",

    @Column(name = "data_exame", nullable = false)
    open var dataExame: LocalDateTime = LocalDateTime.now(),

    // resultado pode ficar vazio: o exame nasce solicitado e so depois e laudado
    @Column(length = 500)
    open var resultado: String? = null,

    // FK para a SUA entidade: o medico que solicitou o exame
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    open var medico: Medico? = null,

    // FK para a entidade do colega: o paciente que fez o exame
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    open var paciente: Paciente? = null
)
