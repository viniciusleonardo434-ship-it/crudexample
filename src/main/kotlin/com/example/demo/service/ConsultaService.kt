package com.example.demo.service

import com.example.demo.model.Consulta
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Contrato do servico de Consulta (passo 12 do roteiro).
 */
@Service
interface ConsultaService {
    fun listarTodas(): List<Consulta>
    fun buscarPorId(id: Long): Optional<Consulta>
    fun salvar(consulta: Consulta): Consulta
    fun atualizar(id: Long, consultaAtualizada: Consulta): Consulta
    fun deletar(id: Long)
}
