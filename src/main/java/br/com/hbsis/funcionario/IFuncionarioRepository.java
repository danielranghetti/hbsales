package br.com.hbsis.funcionario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IFuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
