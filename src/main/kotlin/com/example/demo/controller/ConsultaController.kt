package com.example.demo.controller

import com.example.demo.model.Consulta
import com.example.demo.service.ConsultaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/consultas")
class ConsultaController(private val consultaService: ConsultaService) {

    @GetMapping
    fun listarTodas(): ResponseEntity<List<Consulta>> {
        return ResponseEntity.ok(consultaService.listarTodas())
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Consulta> {
        return consultaService.buscarPorId(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun salvar(@RequestBody consulta: Consulta): ResponseEntity<Consulta> {
        val novaConsulta = consultaService.salvar(consulta)
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConsulta)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody consulta: Consulta): ResponseEntity<Consulta> {
        return try {
            val consultaAtualizada = consultaService.atualizar(id, consulta)
            ResponseEntity.ok(consultaAtualizada)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            consultaService.deletar(id)
            ResponseEntity.noContent().build()
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }
}