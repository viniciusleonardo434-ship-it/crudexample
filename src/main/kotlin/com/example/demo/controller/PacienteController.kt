package com.example.demo.controller

import com.example.demo.model.Paciente
import com.example.demo.service.PacienteService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/pacientes")
class PacienteController(private val pacienteService: PacienteService) {

    @GetMapping
    fun listarTodos(): List<Paciente> = pacienteService.listarTodos()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): Paciente? = pacienteService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody paciente: Paciente): Paciente = pacienteService.salvar(paciente)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody paciente: Paciente): Paciente {
        val pacienteExistente = pacienteService.buscarPorId(id)
        return if (pacienteExistente != null) {
            paciente.nome.takeIf { it.isNotBlank() }?.let { pacienteExistente.nome = it }
            paciente.cpf.takeIf { it.isNotBlank() }?.let { pacienteExistente.cpf = it }
            pacienteService.salvar(pacienteExistente)
        } else {
            pacienteService.salvar(paciente)
        }
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long) = pacienteService.deletar(id)
}