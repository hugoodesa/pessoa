package br.com.pessoa.pessoa.controller;

import br.com.pessoa.pessoa.dto.EnderecoDto;
import br.com.pessoa.pessoa.dto.PessoaDto;
import br.com.pessoa.pessoa.entity.Pessoa;
import br.com.pessoa.pessoa.mapper.EnderecoMapper;
import br.com.pessoa.pessoa.mapper.PessoaMapper;
import br.com.pessoa.pessoa.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("pessoas")
public class PessoaController {

    private final PessoaService service;
    private final PessoaMapper pessoaMapper;
    private final EnderecoMapper enderecoMapper;

    @Autowired
    public PessoaController(PessoaService service, PessoaMapper pessoaMapper, EnderecoMapper enderecoMapper) {
        this.service = service;
        this.pessoaMapper = pessoaMapper;
        this.enderecoMapper = enderecoMapper;
    }

    @PostMapping
    public PessoaDto salvarPessoa(@RequestBody PessoaDto pessoaDto) throws Exception {
        Pessoa pessoa = this.pessoaMapper.toEntity(pessoaDto);

        return this.pessoaMapper.toDto(this.service.save(pessoa));
    }

    @PostMapping("/cadastrarEndereco/{id}")
    @Transactional
    public Pessoa adicionarEndereco (@PathVariable(name = "id") Long idPessoa,@RequestBody EnderecoDto enderecoDto) throws Exception {
        return this.service.adicionarEndereco(idPessoa, this.enderecoMapper.toEntity(enderecoDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws Exception {
        this.service.delete(id);
    }

    @GetMapping
    public List<PessoaDto> findAll(){
        return this.service.findAll();
    }

    @GetMapping("/listaEnderecos/{id}")
    public List<EnderecoDto> findEnderecos(@PathVariable Long id){

        ResponseEntity<Pessoa> pessoa = this.service.findByid(id);

        if(pessoa.getStatusCode() == HttpStatus.OK){
            return pessoa.getBody()
                    .getEnderecos()
                    .stream()
                    .map(e -> enderecoMapper.toDto(e))
                    .toList();
        }

        return new ArrayList<>();
    }

    @Transactional
    @GetMapping("/buscarPessoa/{id}")
    public PessoaDto findById(@PathVariable Long id){
        ResponseEntity<Pessoa> pessoaResposta = this.service.findByid(id);

        if(pessoaResposta.getStatusCode().is2xxSuccessful()){
            return this.pessoaMapper.toDto(pessoaResposta.getBody());
        }

        return new PessoaDto();
    }

    @PutMapping("/atualizar/{id}")
    public PessoaDto atualizarPessoa(@RequestBody PessoaDto pessoaAtualizada,@PathVariable Long id) {
        return this.service.update(pessoaAtualizada,id);
    }

}
