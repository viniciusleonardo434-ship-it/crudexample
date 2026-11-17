package com.example.demo.service

import com.example.demo.model.Consulta
import com.example.demo.repository.ConsultaRepository
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Implementacao do ConsultaService (passo 13 do roteiro).
 */
@Service
class ConsultaServiceImpl(private val consultaRepository: ConsultaRepository) : ConsultaService {

    override fun listarTodas(): List<Consulta> {
        return consultaRepository.findAll()
    }

    override fun buscarPorId(id: Long): Optional<Consulta> {
        return consultaRepository.findById(id)
    }

    override fun salvar(consulta: Consulta): Consulta {
        return consultaRepository.save(consulta)
    }

    override fun atualizar(id: Long, consultaAtualizada: Consulta): Consulta {
        val consultaExistente = consultaRepository.findById(id)
            .orElseThrow { RuntimeException("Consulta não encontrada com o ID: $id") }

        consultaExistente.dataHora = consultaAtualizada.dataHora
        consultaExistente.especialidade = consultaAtualizada.especialidade
        consultaExistente.paciente = consultaAtualizada.paciente

        return consultaRepository.save(consultaExistente)
    }

    override fun deletar(id: Long) {
        if (!consultaRepository.existsById(id)) {
            throw RuntimeException("Consulta não encontrada com o ID: $id")
        }
        consultaRepository.deleteById(id)
    }
}
