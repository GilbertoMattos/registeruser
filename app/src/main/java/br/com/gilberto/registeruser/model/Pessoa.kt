package br.com.gilberto.registeruser.model

import java.io.Serializable
import java.time.LocalDate

data class Pessoa(val nome: String, val nascimento: LocalDate, val sexo: String, val profissao: String, val estadoCivil: String, val email: String, val senha: String, val repeteSenha: String) : Serializable