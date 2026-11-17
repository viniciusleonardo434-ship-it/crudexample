package com.example.demo.repository

import com.example.demo.model.Paciente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface PacienteRepository : JpaRepository<Paciente, Long> {

    // 1. SELECT Nativo por CPF
    @Query(value = "SELECT * FROM paciente WHERE cpf = :cpf", nativeQuery = true)
    fun buscarPorCpfNativo(@Param("cpf") cpf: String): Paciente?

    // 2. INSERT Nativo
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO paciente (nome, cpf) VALUES (:nome, :cpf)", nativeQuery = true)
    fun inserirPacienteNativo(@Param("nome") nome: String, @Param("cpf") cpf: String)

    // 3. UPDATE Nativo
    @Modifying
    @Transactional
    @Query(value = "UPDATE paciente SET nome = :novoNome WHERE id = :id", nativeQuery = true)
    fun atualizarNomeNativo(@Param("id") id: Long, @Param("novoNome") novoNome: String)

    // 4. DELETE Nativo
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM paciente WHERE id = :id", nativeQuery = true)
    fun deletarPacienteNativo(@Param("id") id: Long)
}