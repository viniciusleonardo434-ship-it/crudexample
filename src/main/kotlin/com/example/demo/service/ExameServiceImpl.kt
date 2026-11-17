package com.example.demo.service

import com.example.demo.dto.ExameRequest
import com.example.demo.model.Exame
import com.example.demo.repository.ExameDetalhado
import com.example.demo.repository.ExameRepository
import com.example.demo.repository.MedicoRepository
import com.example.demo.repository.PacienteRepository
import org.springframework.stereotype.Service

/**
 * Implementacao do ExameService (passo 13 do roteiro).
 */
@Service
class ExameServiceImpl(
    private val exameRepository: ExameRepository,
    private val medicoRepository: MedicoRepository,
    private val pacienteRepository: PacienteRepository
) : ExameService {

    override fun listarTodos(): List<Exame> = exameRepository.listarTodosNativo()

    override fun buscarPorId(id: Long): Exame? = exameRepository.findById(id).orElse(null)

    override fun buscarPorMedico(medicoId: Long): List<Exame> =
        exameRepository.buscarPorMedicoNativo(medicoId)

    override fun buscarPorPaciente(pacienteId: Long): List<Exame> =
        exameRepository.buscarPorPacienteNativo(pacienteId)

    override fun relatorioDetalhado(): List<ExameDetalhado> =
        exameRepository.relatorioDetalhadoNativo()

    override fun criar(request: ExameRequest): Exame {
        // integridade referencial: nao adianta o banco ter FK se a API deixar
        // o usuario mandar um id que nao existe e so descobrir no erro do MySQL
        if (!medicoRepository.existsById(request.medicoId)) {
            throw IllegalArgumentException("Medico nao encontrado com o ID: ${request.medicoId}")
        }
        if (!pacienteRepository.existsById(request.pacienteId)) {
            throw IllegalArgumentException("Paciente nao encontrado com o ID: ${request.pacienteId}")
        }

        exameRepository.inserirExameNativo(
            tipo = request.tipo,
            resultado = request.resultado,
            medicoId = request.medicoId,
            pacienteId = request.pacienteId
        )

        return exameRepository.buscarUltimoInseridoNativo()
            ?: throw IllegalStateException("Exame inserido mas nao encontrado apos o INSERT")
    }

    override fun atualizar(id: Long, request: ExameRequest): Exame {
        val linhasAfetadas = exameRepository.atualizarExameNativo(
            id = id,
            tipo = request.tipo,
            resultado = request.resultado
        )

        if (linhasAfetadas == 0) {
            throw NoSuchElementException("Exame nao encontrado com o ID: $id")
        }

        return buscarPorId(id) ?: throw IllegalStateException("Exame atualizado mas nao encontrado")
    }

    override fun deletar(id: Long) {
        val linhasAfetadas = exameRepository.deletarExameNativo(id)
        if (linhasAfetadas == 0) {
            throw NoSuchElementException("Exame nao encontrado com o ID: $id")
        }
    }

    /**
     * Apaga os exames de um medico antes de apagar o proprio medico.
     * Sem isso o MySQL recusa o DELETE do medico por causa da chave estrangeira.
     */
    override fun deletarPorMedico(medicoId: Long): Int =
        exameRepository.deletarPorMedicoNativo(medicoId)
}
