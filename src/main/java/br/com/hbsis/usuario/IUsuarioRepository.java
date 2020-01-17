package br.com.hbsis.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Classe responsável pela comunciação com o banco de dados
 */
@Repository
interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

}
