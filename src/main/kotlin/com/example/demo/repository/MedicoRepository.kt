package com.example.demo.repository

import com.example.demo.model.Medico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface MedicoRepository : JpaRepository<Medico, Long> {

    // 1. SELECT nativo - lista todos os medicos
    @Query(value = "SELECT * FROM medico", nativeQuery = true)
    fun listarTodosNativo(): List<Medico>

    // 2. SELECT nativo - busca um medico pelo CRM (campo unico)
    @Query(value = "SELECT * FROM medico WHERE crm = :crm", nativeQuery = true)
    fun buscarPorCrmNativo(@Param("crm") crm: String): Medico?

    // 3. SELECT nativo - filtra medicos por especialidade
    @Query(value = "SELECT * FROM medico WHERE especialidade = :especialidade", nativeQuery = true)
    fun buscarPorEspecialidadeNativo(@Param("especialidade") especialidade: String): List<Medico>

    // 4. INSERT nativo
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "INSERT INTO medico (nome, crm, especialidade, ativo) VALUES (:nome, :crm, :especialidade, :ativo)",
        nativeQuery = true
    )
    fun inserirMedicoNativo(
        @Param("nome") nome: String,
        @Param("crm") crm: String,
        @Param("especialidade") especialidade: String,
        @Param("ativo") ativo: Boolean
    )

    // 5. UPDATE nativo
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE medico SET nome = :nome, especialidade = :especialidade, ativo = :ativo WHERE id = :id",
        nativeQuery = true
    )
    fun atualizarMedicoNativo(
        @Param("id") id: Long,
        @Param("nome") nome: String,
        @Param("especialidade") especialidade: String,
        @Param("ativo") ativo: Boolean
    ): Int

    // 6. DELETE nativo
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "DELETE FROM medico WHERE id = :id", nativeQuery = true)
    fun deletarMedicoNativo(@Param("id") id: Long): Int
}
