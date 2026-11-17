package com.example.demo.repository

import com.example.demo.model.Consulta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ConsultaRepository : JpaRepository<Consulta, Long> {

    // 1. SELECT Nativo: busca todas as consultas de um paciente específico pelo ID dele (FK)
    @Query(value = "SELECT * FROM consulta WHERE paciente_id = :pacienteId", nativeQuery = true)
    fun buscarConsultasPorPacienteNativo(@Param("pacienteId") pacienteId: Long): List<Consulta>

    // 2. INSERT Nativo: insere a consulta amarrando o paciente_id
    @Modifying
    @Transactional
    @Query(
        value = "INSERT INTO consulta (data_hora, especialidade, paciente_id) VALUES (NOW(), :especialidade, :pacienteId)",
        nativeQuery = true
    )
    fun agendarConsultaNativa(@Param("especialidade") especialidade: String, @Param("pacienteId") pacienteId: Long)
}