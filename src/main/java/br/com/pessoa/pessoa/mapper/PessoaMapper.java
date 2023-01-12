package br.com.pessoa.pessoa.mapper;

import br.com.pessoa.pessoa.dto.PessoaDto;
import br.com.pessoa.pessoa.entity.Pessoa;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PessoaMapper {

    private EnderecoMapper enderecoMapper;

    @Autowired
    public PessoaMapper(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }

    public Pessoa toEntity(PessoaDto pessoaDto) {
        return Pessoa.builder()
                .id(pessoaDto.getId())
                .nome(pessoaDto.getNome())
                .dataNascimento(pessoaDto.getDataNascimento())
                .enderecos(this.enderecoMapper.toListEntity(pessoaDto.getEnderecos()))
                .build();
    }

    public PessoaDto toDto(Pessoa pessoa) {
        return PessoaDto.builder()
                .id(pessoa.getId())
                .nome(pessoa.getNome())
                .dataNascimento(pessoa.getDataNascimento())
                .enderecos(this.enderecoMapper.toListDto(pessoa.getEnderecos()))
                .build();
    }


}
