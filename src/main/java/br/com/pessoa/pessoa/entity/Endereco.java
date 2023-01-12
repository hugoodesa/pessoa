package br.com.pessoa.pessoa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "endereco")

public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String logradouro;
    @NotBlank
    private String cidade;
    @NotBlank
    private String cep;
    private Integer numero;
    private Boolean isEnderecoPrincipal;

}