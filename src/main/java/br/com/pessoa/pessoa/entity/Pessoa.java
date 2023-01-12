package br.com.pessoa.pessoa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "pessoa")
@ToString
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private String nome;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDate dataNascimento;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa")
    private List<Endereco> enderecos;

}