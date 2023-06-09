package br.travelexpense.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.travelexpense.model.Cliente;
import br.travelexpense.model.Despesa;
import br.travelexpense.model.Endereco;
import br.travelexpense.model.Funcionario;
import br.travelexpense.model.Orcamento;
import br.travelexpense.model.Viagem;
import br.travelexpense.utils.RandomNameGenerator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ViagemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    public void listaDeViagens() throws Exception{
        this.mockMvc.perform(get("/viagem/list")
        .header("Authorization", "Basic dXNlcjplNDZhZjMyMS02NGY5LTQxZDItOTU3OC00NWQ0YmU0YzRmMGQ="))
        .andExpect(status().isOk());
    }
    
    @Test
    public void adicionarViagem() throws Exception{
        Viagem viagem = new Viagem();
        List<Despesa> listaDespesas = new ArrayList<Despesa>();
        RandomNameGenerator random = new RandomNameGenerator();
        Date dataAux = new Date(random.generateRandomLong());
        List<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();
        Orcamento orcamento = new Orcamento();
        viagem.setDtFim(dataAux);
        viagem.setDtInicio(dataAux);
        viagem.setDespesas(listaDespesas);
        viagem.setFuncionarios(listaFuncionarios);
        viagem.setId(1l);
        viagem.setOrcamento(orcamento);
        viagem.setAnotacoes(random.generateRandomName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(viagem);
        this.mockMvc.perform(post("/viagem/add/")
        .header("Authorization", "Basic dXNlcjplNDZhZjMyMS02NGY5LTQxZDItOTU3OC00NWQ0YmU0YzRmMGQ=").contentType(APPLICATION_JSON_UTF8).content(requestJson))
        .andExpect(status().isOk());
    }

    @Test
    public void verificarViagemPorId() throws Exception{
        Viagem viagem = new Viagem();
        List<Despesa> listaDespesas = new ArrayList<Despesa>();
        RandomNameGenerator random = new RandomNameGenerator();
        Date dataAux = new Date(random.generateRandomLong());
        List<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();
        Orcamento orcamento = new Orcamento();
        viagem.setDtFim(dataAux);
        viagem.setDtInicio(dataAux);
        viagem.setDespesas(listaDespesas);
        viagem.setFuncionarios(listaFuncionarios);
        viagem.setId(1l);
        viagem.setOrcamento(orcamento);
        viagem.setAnotacoes(random.generateRandomName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(viagem);
        MvcResult result = this.mockMvc.perform(post("/viagem/add/")
        .header("Authorization", "Basic dXNlcjplNDZhZjMyMS02NGY5LTQxZDItOTU3OC00NWQ0YmU0YzRmMGQ=").contentType(APPLICATION_JSON_UTF8).content(requestJson))
        .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);
        String viagemId = jsonResponse.get("id").asText();
        
        this.mockMvc.perform(get("/viagem/get/"+viagemId)
        .header("Authorization", "Basic dXNlcjplNDZhZjMyMS02NGY5LTQxZDItOTU3OC00NWQ0YmU0YzRmMGQ="))
        .andExpect(status().isOk());
    }


}
