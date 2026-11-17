package com.example.demo.service

import com.example.demo.model.Medico
import com.example.demo.repository.MedicoRepository
import org.springframework.stereotype.Service

/**
 * Implementacao do MedicoService (passo 13 do roteiro).
 * O @Service aqui e o que faz o Spring criar o bean e injetar no Controller.
 */
@Service
class MedicoServiceImpl(private val medicoRepository: MedicoRepository) : MedicoService {

    override fun listarTodos(): List<Medico> = medicoRepository.listarTodosNativo()

    override fun buscarPorId(id: Long): Medico? = medicoRepository.findById(id).orElse(null)

    override fun buscarPorCrm(crm: String): Medico? = medicoRepository.buscarPorCrmNativo(crm)

    override fun buscarPorEspecialidade(especialidade: String): List<Medico> =
        medicoRepository.buscarPorEspecialidadeNativo(especialidade)

    override fun criar(medico: Medico): Medico {
        if (medicoRepository.buscarPorCrmNativo(medico.crm) != null) {
            throw IllegalArgumentException("Ja existe um medico cadastrado com o CRM ${medico.crm}")
        }

        medicoRepository.inserirMedicoNativo(
            nome = medico.nome,
            crm = medico.crm,
            especialidade = medico.especialidade,
            ativo = medico.ativo
        )

        // o INSERT nativo nao devolve o registro, entao buscamos pelo CRM para retornar o id gerado
        return medicoRepository.buscarPorCrmNativo(medico.crm)
            ?: throw IllegalStateException("Medico inserido mas nao encontrado apos o INSERT")
    }

    override fun atualizar(id: Long, medico: Medico): Medico {
        val linhasAfetadas = medicoRepository.atualizarMedicoNativo(
            id = id,
            nome = medico.nome,
            especialidade = medico.especialidade,
            ativo = medico.ativo
        )

        if (linhasAfetadas == 0) {
            throw NoSuchElementException("Medico nao encontrado com o ID: $id")
        }

        return buscarPorId(id) ?: throw IllegalStateException("Medico atualizado mas nao encontrado")
    }

    override fun deletar(id: Long) {
        val linhasAfetadas = medicoRepository.deletarMedicoNativo(id)
        if (linhasAfetadas == 0) {
            throw NoSuchElementException("Medico nao encontrado com o ID: $id")
        }
    }
}
