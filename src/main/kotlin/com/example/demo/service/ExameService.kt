package com.example.demo.service

import com.example.demo.dto.ExameRequest
import com.example.demo.model.Exame
import com.example.demo.repository.ExameDetalhado
import org.springframework.stereotype.Service

/**
 * Contrato do servico de Exame (passo 12 do roteiro).
 */
@Service
interface ExameService {
    fun listarTodos(): List<Exame>
    fun buscarPorId(id: Long): Exame?
    fun buscarPorMedico(medicoId: Long): List<Exame>
    fun buscarPorPaciente(pacienteId: Long): List<Exame>
    fun relatorioDetalhado(): List<ExameDetalhado>
    fun criar(request: ExameRequest): Exame
    fun atualizar(id: Long, request: ExameRequest): Exame
    fun deletar(id: Long)
    fun deletarPorMedico(medicoId: Long): Int
}
