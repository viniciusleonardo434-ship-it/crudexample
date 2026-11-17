package com.example.demo.controller

import com.example.demo.model.Medico
import com.example.demo.service.MedicoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Medicos", description = "CRUD de medicos usando SQL nativo")
class MedicoController(private val medicoService: MedicoService) {

    @GetMapping
    @Operation(summary = "Lista todos os medicos (SELECT nativo)")
    fun listarTodos(): ResponseEntity<List<Medico>> =
        ResponseEntity.ok(medicoService.listarTodos())

    @GetMapping("/{id}")
    @Operation(summary = "Busca um medico pelo ID")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Medico> {
        val medico = medicoService.buscarPorId(id)
        return if (medico != null) ResponseEntity.ok(medico)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/crm/{crm}")
    @Operation(summary = "Busca um medico pelo CRM (SELECT nativo)")
    fun buscarPorCrm(@PathVariable crm: String): ResponseEntity<Medico> {
        val medico = medicoService.buscarPorCrm(crm)
        return if (medico != null) ResponseEntity.ok(medico)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/especialidade/{especialidade}")
    @Operation(summary = "Filtra medicos por especialidade (SELECT nativo)")
    fun buscarPorEspecialidade(@PathVariable especialidade: String): ResponseEntity<List<Medico>> =
        ResponseEntity.ok(medicoService.buscarPorEspecialidade(especialidade))

    @PostMapping
    @Operation(summary = "Cadastra um medico (INSERT nativo)")
    fun criar(@RequestBody medico: Medico): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(medicoService.criar(medico))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("erro" to e.message))
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um medico (UPDATE nativo)")
    fun atualizar(@PathVariable id: Long, @RequestBody medico: Medico): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(medicoService.atualizar(id, medico))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um medico (DELETE nativo)")
    fun deletar(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            medicoService.deletar(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        } catch (e: DataIntegrityViolationException) {
            // o MySQL recusou o DELETE porque existem exames apontando para esse medico.
            // e a chave estrangeira protegendo o banco de ficar com registro orfao.
            ResponseEntity.status(HttpStatus.CONFLICT).body(
                mapOf("erro" to "Nao e possivel excluir: este medico possui exames vinculados. Exclua os exames primeiro.")
            )
        }
    }
}
