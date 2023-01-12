package br.com.pessoa.pessoa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDto implements Serializable {

    private Long id;
    private String nome;
    @JsonFormat(pattern = "dd/MM/yyyy",shape = JsonFormat.Shape.STRING)
    private List<EnderecoDto> enderecos;
    private LocalDate dataNascimento;

}
