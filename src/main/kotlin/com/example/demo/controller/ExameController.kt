package com.example.demo.controller

import com.example.demo.dto.ExameRequest
import com.example.demo.model.Exame
import com.example.demo.repository.ExameDetalhado
import com.example.demo.service.ExameService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exames")
@Tag(name = "Exames", description = "CRUD de exames usando SQL nativo, com FK para Medico e Paciente")
class ExameController(private val exameService: ExameService) {

    @GetMapping
    @Operation(summary = "Lista todos os exames (SELECT nativo)")
    fun listarTodos(): ResponseEntity<List<Exame>> =
        ResponseEntity.ok(exameService.listarTodos())

    @GetMapping("/{id}")
    @Operation(summary = "Busca um exame pelo ID")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Exame> {
        val exame = exameService.buscarPorId(id)
        return if (exame != null) ResponseEntity.ok(exame)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/medico/{medicoId}")
    @Operation(summary = "Exames solicitados por um medico (SELECT nativo pela FK)")
    fun buscarPorMedico(@PathVariable medicoId: Long): ResponseEntity<List<Exame>> =
        ResponseEntity.ok(exameService.buscarPorMedico(medicoId))

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Exames de um paciente (SELECT nativo pela FK)")
    fun buscarPorPaciente(@PathVariable pacienteId: Long): ResponseEntity<List<Exame>> =
        ResponseEntity.ok(exameService.buscarPorPaciente(pacienteId))

    @GetMapping("/relatorio")
    @Operation(summary = "Relatorio com INNER JOIN de exame + medico + paciente")
    fun relatorio(): ResponseEntity<List<ExameDetalhado>> =
        ResponseEntity.ok(exameService.relatorioDetalhado())

    @PostMapping
    @Operation(summary = "Cadastra um exame (INSERT nativo)")
    fun criar(@RequestBody request: ExameRequest): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(exameService.criar(request))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("erro" to e.message))
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza / lauda um exame (UPDATE nativo)")
    fun atualizar(@PathVariable id: Long, @RequestBody request: ExameRequest): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(exameService.atualizar(id, request))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um exame (DELETE nativo)")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            exameService.deletar(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }
}
