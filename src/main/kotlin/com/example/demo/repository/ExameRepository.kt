package com.example.demo.repository

import com.example.demo.model.Exame
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Projecao: molde de leitura para a query com JOIN.
 *
 * O resultado do JOIN nao cabe em nenhuma entidade (mistura colunas de 3 tabelas),
 * entao declaramos uma interface com um getter por coluna. O Spring Data casa
 * cada apelido (AS) da query com a propriedade de mesmo nome aqui.
 */
interface ExameDetalhado {
    val id: Long
    val tipo: String
    val data: String
    val medico: String
    val crm: String
    val paciente: String
}

@Repository
interface ExameRepository : JpaRepository<Exame, Long> {

    // 1. SELECT nativo - lista todos os exames
    @Query(value = "SELECT * FROM exame", nativeQuery = true)
    fun listarTodosNativo(): List<Exame>

    // 2. SELECT nativo - exames solicitados por um medico (usa a FK medico_id)
    @Query(value = "SELECT * FROM exame WHERE medico_id = :medicoId", nativeQuery = true)
    fun buscarPorMedicoNativo(@Param("medicoId") medicoId: Long): List<Exame>

    // 3. SELECT nativo - exames de um paciente (usa a FK paciente_id)
    @Query(value = "SELECT * FROM exame WHERE paciente_id = :pacienteId", nativeQuery = true)
    fun buscarPorPacienteNativo(@Param("pacienteId") pacienteId: Long): List<Exame>

    // 4. SELECT nativo com INNER JOIN de 3 tabelas - o relatorio da apresentacao
    @Query(
        value = """
            SELECT  e.id                                  AS id,
                    e.tipo                                AS tipo,
                    DATE_FORMAT(e.data_exame, '%d/%m/%Y') AS data,
                    m.nome                                AS medico,
                    m.crm                                 AS crm,
                    p.nome                                AS paciente
            FROM exame e
            INNER JOIN medico   m ON m.id = e.medico_id
            INNER JOIN paciente p ON p.id = e.paciente_id
            ORDER BY e.data_exame DESC
        """,
        nativeQuery = true
    )
    fun relatorioDetalhadoNativo(): List<ExameDetalhado>

    // 5. INSERT nativo - NOW() preenche a data no proprio banco
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = """
            INSERT INTO exame (tipo, data_exame, resultado, medico_id, paciente_id)
            VALUES (:tipo, NOW(), :resultado, :medicoId, :pacienteId)
        """,
        nativeQuery = true
    )
    fun inserirExameNativo(
        @Param("tipo") tipo: String,
        @Param("resultado") resultado: String?,
        @Param("medicoId") medicoId: Long,
        @Param("pacienteId") pacienteId: Long
    )

    // 6b. SELECT nativo - o INSERT nativo nao devolve o id gerado,
    //     entao buscamos o ultimo registro inserido para poder retorna-lo na API
    @Query(value = "SELECT * FROM exame ORDER BY id DESC LIMIT 1", nativeQuery = true)
    fun buscarUltimoInseridoNativo(): Exame?

    // 6. UPDATE nativo - o caso de uso real: laudar o exame
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE exame SET tipo = :tipo, resultado = :resultado WHERE id = :id",
        nativeQuery = true
    )
    fun atualizarExameNativo(
        @Param("id") id: Long,
        @Param("tipo") tipo: String,
        @Param("resultado") resultado: String?
    ): Int

    // 7. DELETE nativo
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "DELETE FROM exame WHERE id = :id", nativeQuery = true)
    fun deletarExameNativo(@Param("id") id: Long): Int

    // 8. DELETE nativo em cascata manual - apaga os exames de um medico
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "DELETE FROM exame WHERE medico_id = :medicoId", nativeQuery = true)
    fun deletarPorMedicoNativo(@Param("medicoId") medicoId: Long): Int
}
