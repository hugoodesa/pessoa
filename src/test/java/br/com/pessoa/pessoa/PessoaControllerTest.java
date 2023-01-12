package br.com.pessoa.pessoa;

import br.com.pessoa.pessoa.dto.EnderecoDto;
import br.com.pessoa.pessoa.dto.PessoaDto;
import br.com.pessoa.pessoa.dto.PessoaDtoTest;
import br.com.pessoa.pessoa.entity.Endereco;
import br.com.pessoa.pessoa.entity.Pessoa;
import br.com.pessoa.pessoa.service.PessoaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@SpringBootTest
public class PessoaControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private PessoaService pessoaService;

    @BeforeEach
    void setUp() {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        this.mockMvc = webAppContextSetup(this.context).build();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(df);

    }

    //TODO : Criar um teste para garantir que não existiram dois endereços principais

    @Test
    void cadastrarPessoaTest() throws Exception {
        PessoaDto pessoaDto = PessoaDto
                .builder()
                .nome("Hugo")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(73)
                                .isEnderecoPrincipal(false)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(true)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();

        var json = objectMapper.writeValueAsString(pessoaDto);

        String response = mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        pessoaDto = objectMapper.readValue(response, PessoaDto.class);

        assertNotNull(pessoaDto.getId());
        assertEquals("Hugo", pessoaDto.getNome());
        assertEquals(2, pessoaDto.getEnderecos().size());
        assertEquals("Rua Paulino Alexandre da Silva", pessoaDto.getEnderecos().get(0).getLogradouro());
    }

    @Test
    void quandoPessoaTemMaisDeUmEnderecoPrincipalAtivaDeveEmitirExceptionTest() throws Exception {
        PessoaDto pessoaDto = PessoaDto
                .builder()
                .nome("Hugo")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(73)
                                .isEnderecoPrincipal(true)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(true)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();

        var json = objectMapper.writeValueAsString(pessoaDto);

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(post("/pessoas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        });

        assertEquals("Não é possível mais de um endereco", exception.getCause().getMessage());

    }

    @Test
    void quandoExcluirPessoaPorIdDeveRemoveDaBaseTest() throws Exception {
        PessoaDto pessoaDto = PessoaDto
                .builder()
                .nome("Hugo")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(73)
                                .isEnderecoPrincipal(true)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(false)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();


        var json = objectMapper.writeValueAsString(pessoaDto);

        String response = mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        pessoaDto = objectMapper.readValue(response, PessoaDto.class);

        mockMvc.perform(delete("/pessoas/" + pessoaDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(this.pessoaService.findByid(pessoaDto.getId()), ResponseEntity.badRequest().build());
    }


    @Test
    void bucarTodasAsPessoasDaBaseTest() throws Exception {

        PessoaDto pessoaDto = PessoaDto
                .builder()
                .nome("Hugo")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(73)
                                .isEnderecoPrincipal(true)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(false)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();


        var json = objectMapper.writeValueAsString(pessoaDto);

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PessoaDto> pessoas = this.pessoaService.findAll();

        assertEquals(pessoas.size(), 1);
    }

    @Test
    void deveBuscarEnderecosPessoaTest() throws Exception {

        PessoaDto pessoaDto = PessoaDto
                .builder()
                .nome("Hugo")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(73)
                                .isEnderecoPrincipal(false)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(true)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();

        var json = objectMapper.writeValueAsString(pessoaDto);

        String response = mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        pessoaDto = objectMapper.readValue(response, PessoaDto.class);

        response = mockMvc.perform(get("/pessoas/listaEnderecos/"+pessoaDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<EnderecoDto> enderecoDto = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, EnderecoDto.class));

        assertEquals(enderecoDto.size(),2);

    }

    @Test
    void deveBuscarUmaPessoaTest() throws Exception {

        PessoaDto pessoaDto = PessoaDto
                .builder()
                .nome("Hugo")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(73)
                                .isEnderecoPrincipal(false)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(true)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();

        var json = objectMapper.writeValueAsString(pessoaDto);

        String response = mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        pessoaDto = objectMapper.readValue(response, PessoaDto.class);

        response = mockMvc.perform(get("/pessoas/buscarPessoa/"+pessoaDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PessoaDto pessoa = objectMapper.readValue(response, PessoaDto.class);

        assertEquals(pessoa.getNome(),"Hugo");
        assertEquals(pessoa.getEnderecos().get(0).getLogradouro(),"Rua Paulino Alexandre da Silva");

    }

    @Test
    void deveAtualizarUmaPessoaTest() throws Exception {

        PessoaDto pessoaDto = PessoaDto
                .builder()
                .nome("Hugo")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(73)
                                .isEnderecoPrincipal(false)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(true)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();

        var json = objectMapper.writeValueAsString(pessoaDto);

        String response = mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        pessoaDto = objectMapper.readValue(response, PessoaDto.class);

        Long id = pessoaDto.getId();

        pessoaDto = PessoaDto
                .builder()
                .nome("Hugo Luiz Stapassoli")
                .dataNascimento(LocalDate.now())
                .enderecos(Arrays.asList(
                        EnderecoDto.builder()
                                .cidade("Capivari")
                                .numero(73)
                                .isEnderecoPrincipal(false)
                                .cep("88745-000")
                                .logradouro("Rua Paulino Alexandre da Silva")
                                .build(),
                        EnderecoDto.builder()
                                .cidade("Capivari De Baixo")
                                .numero(75)
                                .isEnderecoPrincipal(true)
                                .cep("88745-001")
                                .logradouro("Rua Santo Silva")
                                .build()))
                .build();

        json = objectMapper.writeValueAsString(pessoaDto);

        response = mockMvc.perform(put("/pessoas/atualizar/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PessoaDto pessoa = objectMapper.readValue(response, PessoaDto.class);

        assertEquals(pessoa.getNome(),"Hugo Luiz Stapassoli");
        assertEquals(pessoa.getEnderecos().get(0).getCidade(),"Capivari");

    }

}
