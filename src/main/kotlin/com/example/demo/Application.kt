package com.example.demo

import com.example.demo.model.Consulta
import com.example.demo.model.Paciente
import com.example.demo.repository.ConsultaRepository
import com.example.demo.repository.ExameRepository
import com.example.demo.repository.MedicoRepository
import com.example.demo.repository.PacienteRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.LocalDateTime

@SpringBootApplication
class Application {

	@Bean
	fun carregarDadosIniciais(
		pacienteRepository: PacienteRepository,
		consultaRepository: ConsultaRepository,
		medicoRepository: MedicoRepository,
		exameRepository: ExameRepository
	) = CommandLineRunner {
		println("=== INICIALIZANDO BANCO DE DADOS ===")

		try {
			// ----- entidades do colega: Paciente e Consulta -----
			val pacienteExistente = pacienteRepository.findAll().firstOrNull { it.cpf == "123.456.789-00" }

			val paciente = pacienteExistente ?: pacienteRepository.save(
				Paciente(
					nome = "Carlos Silva Santos",
					cpf = "123.456.789-00"
				)
			)

			if (consultaRepository.findAll().none { it.paciente?.id == paciente.id }) {
				consultaRepository.save(
					Consulta(
						dataHora = LocalDateTime.now(),
						especialidade = "Cardiologia",
						paciente = paciente
					)
				)
				consultaRepository.save(
					Consulta(
						dataHora = LocalDateTime.now(),
						especialidade = "Ortopedia",
						paciente = paciente
					)
				)
			}

			// ----- entidades do Rafael: Medico e Exame -----
			// inseridos com as queries nativas, para ja provar que elas funcionam no boot
			if (medicoRepository.buscarPorCrmNativo("CRM-BA 12345") == null) {
				medicoRepository.inserirMedicoNativo(
					nome = "Ana Beatriz Nogueira",
					crm = "CRM-BA 12345",
					especialidade = "Cardiologia",
					ativo = true
				)
			}
			if (medicoRepository.buscarPorCrmNativo("CRM-BA 67890") == null) {
				medicoRepository.inserirMedicoNativo(
					nome = "Rodrigo Alves Pinto",
					crm = "CRM-BA 67890",
					especialidade = "Ortopedia",
					ativo = true
				)
			}

			val cardiologista = medicoRepository.buscarPorCrmNativo("CRM-BA 12345")

			if (cardiologista != null && exameRepository.buscarPorMedicoNativo(cardiologista.id).isEmpty()) {
				exameRepository.inserirExameNativo(
					tipo = "Eletrocardiograma",
					resultado = null,
					medicoId = cardiologista.id,
					pacienteId = paciente.id
				)
				exameRepository.inserirExameNativo(
					tipo = "Hemograma completo",
					resultado = "Sem alteracoes significativas",
					medicoId = cardiologista.id,
					pacienteId = paciente.id
				)
			}

			println("=== CARGA INICIAL CONCLUIDA ===")
		} catch (e: Exception) {
			println("Aviso na carga inicial: ${e.message}")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
