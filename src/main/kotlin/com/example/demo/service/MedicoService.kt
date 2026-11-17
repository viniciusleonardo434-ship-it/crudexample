package com.example.demo.service

import com.example.demo.model.Medico
import org.springframework.stereotype.Service

/**
 * Contrato do servico de Medico (passo 12 do roteiro).
 *
 * A interface diz O QUE o sistema sabe fazer com medico.
 * A implementacao (MedicoServiceImpl) diz COMO faz.
 *
 * O Controller depende so desta interface: se um dia a implementacao mudar
 * (de SQL nativo para JPA, por exemplo), o Controller nao precisa ser tocado.
 */
@Service
interface MedicoService {
    fun listarTodos(): List<Medico>
    fun buscarPorId(id: Long): Medico?
    fun buscarPorCrm(crm: String): Medico?
    fun buscarPorEspecialidade(especialidade: String): List<Medico>
    fun criar(medico: Medico): Medico
    fun atualizar(id: Long, medico: Medico): Medico
    fun deletar(id: Long)
}
