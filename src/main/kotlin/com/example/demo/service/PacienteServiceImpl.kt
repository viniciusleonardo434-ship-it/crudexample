package com.example.demo.service

import com.example.demo.model.Paciente
import com.example.demo.repository.PacienteRepository
import org.springframework.stereotype.Service

/**
 * Implementacao do PacienteService (passo 13 do roteiro).
 */
@Service
class PacienteServiceImpl(private val pacienteRepository: PacienteRepository) : PacienteService {

    override fun listarTodos(): List<Paciente> = pacienteRepository.findAll()

    override fun buscarPorId(id: Long): Paciente? = pacienteRepository.findById(id).orElse(null)

    override fun salvar(paciente: Paciente): Paciente = pacienteRepository.save(paciente)

    override fun deletar(id: Long) = pacienteRepository.deleteById(id)
}
