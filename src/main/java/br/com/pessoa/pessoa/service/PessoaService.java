package br.com.pessoa.pessoa.service;

import br.com.pessoa.pessoa.dto.PessoaDto;
import br.com.pessoa.pessoa.entity.Endereco;
import br.com.pessoa.pessoa.entity.Pessoa;
import br.com.pessoa.pessoa.mapper.PessoaMapper;
import br.com.pessoa.pessoa.repository.PessoaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    PessoaRepository repository;

    @Autowired
    PessoaMapper mapper;

    @Transactional
    public Pessoa save(Pessoa pessoa) throws Exception {

        long count = pessoa.getEnderecos()
                .stream().
                filter(Endereco::getIsEnderecoPrincipal)
                .count();

        if (count > 1) {
            //Todo : Criar exception customiza
            throw new Exception("Não é possível mais de um endereco");
        }

        return this.repository.save(pessoa);
    }

    @Transactional
    public Pessoa adicionarEndereco(Long idPessoa, Endereco endereco) throws Exception {

        Optional<Pessoa> pessoaEncontrada = this.repository.findById(idPessoa);

        if (pessoaEncontrada.isPresent()) {

            if (endereco.getIsEnderecoPrincipal()) {
                pessoaEncontrada.get().getEnderecos().forEach(end -> end.setIsEnderecoPrincipal(false));
            }

            pessoaEncontrada.get().getEnderecos().add(endereco);

            this.repository.save(pessoaEncontrada.get());

        }

        throw new Exception("Pessoa não encontrada com o ID: " + idPessoa);

    }

    @Transactional
    public void delete(Long id) throws Exception {
        this.repository.deleteById(id);
    }

    public List<PessoaDto> findAll() {
        return this.repository.findAll().stream().map(p -> mapper.toDto(p)).toList();
    }


    public PessoaDto update(PessoaDto pessoaAtualizada, Long id) {
        Optional<Pessoa> pessoaEncontrada = this.repository.findById(id);

        if(pessoaEncontrada.isPresent()){
            pessoaAtualizada.setId(id);
            Pessoa pessoaSalva = this.repository.save(this.mapper.toEntity(pessoaAtualizada));
            this.mapper.toDto(pessoaSalva);
        }

        return new PessoaDto();
    }

    public ResponseEntity<Pessoa> findByid(Long id) {
        try {
            Optional<Pessoa> pessoa = this.repository.findById(id);

            if (pessoa.isPresent()) {
                return ResponseEntity.ok(pessoa.get());
            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
