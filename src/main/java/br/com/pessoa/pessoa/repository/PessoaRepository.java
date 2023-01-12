package br.com.pessoa.pessoa.repository;

import br.com.pessoa.pessoa.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}