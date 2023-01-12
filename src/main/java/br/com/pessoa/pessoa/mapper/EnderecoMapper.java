package br.com.pessoa.pessoa.mapper;

import br.com.pessoa.pessoa.dto.EnderecoDto;
import br.com.pessoa.pessoa.entity.Endereco;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoDto endereco) {
        return Endereco.builder()
                .id(endereco.getId())
                .logradouro(endereco.getLogradouro())
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .numero(endereco.getNumero())
                .isEnderecoPrincipal(endereco.getIsEnderecoPrincipal())
                .build();
    }

    public EnderecoDto toDto(Endereco endereco) {
        return EnderecoDto.builder()
                .id(endereco.getId())
                .logradouro(endereco.getLogradouro())
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .numero(endereco.getNumero())
                .isEnderecoPrincipal(endereco.getIsEnderecoPrincipal())
                .build();
    }

    public List<EnderecoDto> toListDto (List<Endereco> enderecos){
        return enderecos
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<Endereco> toListEntity (List<EnderecoDto> enderecosDto){
        return enderecosDto
                .stream()
                .map(this::toEntity)
                .toList();
    }

}
