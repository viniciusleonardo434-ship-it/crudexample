package com.example.demo.service

import com.example.demo.model.Paciente
import org.springframework.stereotype.Service

/**
 * Contrato do servico de Paciente (passo 12 do roteiro).
 */
@Service
interface PacienteService {
    fun listarTodos(): List<Paciente>
    fun buscarPorId(id: Long): Paciente?
    fun salvar(paciente: Paciente): Paciente
    fun deletar(id: Long)
}
