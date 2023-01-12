package br.com.pessoa.pessoa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnderecoDto implements Serializable {

    private Long id;
    private String logradouro;
    private String cidade;
    private String cep;
    private Integer numero;
    private Boolean isEnderecoPrincipal;
}