package br.com.pessoa.pessoa.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDtoTest implements Serializable {

    private Long id;
    private String nome;
    private List<EnderecoDto> enderecos;

}
