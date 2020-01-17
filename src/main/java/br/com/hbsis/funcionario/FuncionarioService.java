package br.com.hbsis.funcionario;


import br.com.hbsis.conexao.EmployeeSalvaDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FuncionarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);

    private final ConexaoFuncionario conexaoFuncionario;

    public FuncionarioService(ConexaoFuncionario conexaoFuncionario) {
        this.conexaoFuncionario = conexaoFuncionario;

    }

    public  FuncionarioDTO save(FuncionarioDTO funcionarioDTO){

        this.validate(funcionarioDTO);

        LOGGER.info("Salvando Funcionário");
        LOGGER.debug("Funcionario: {}",funcionarioDTO);

        Funcionario funcionario = new Funcionario();

        funcionario.setId(funcionarioDTO.getId());
        funcionario.seteMail(funcionarioDTO.geteMail());
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setUuid(UUID.randomUUID().toString());

        funcionario = this.conexaoFuncionario.save(funcionario);
        return funcionarioDTO.of(funcionario);
    }

    private void validate(FuncionarioDTO funcionarioDTO){

        LOGGER.info("Validando Funcionário");

        if (funcionarioDTO == null){
            throw new  IllegalArgumentException("FuncionarioDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(funcionarioDTO.getNome())){
            throw new IllegalArgumentException("O nome do funcionário não deve ser nulo");
        }
        if (StringUtils.isEmpty(funcionarioDTO.geteMail())){
            throw new  IllegalArgumentException("O e-mail não deve ser nulo");
        }
        hbEmployeeValidarFuncionario(funcionarioDTO);
        if (StringUtils.isEmpty(funcionarioDTO.getUuid())){
            throw new IllegalArgumentException("UUID do funcionário não deve ser nulo");
        }
    }
    public  FuncionarioDTO findById(Long id){
        Optional<Funcionario>funcionarioOptional = this.conexaoFuncionario.findById(id);

        if (funcionarioOptional.isPresent()){
            return  FuncionarioDTO.of(funcionarioOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Funcionario findByFuncionarioId(Long id) {
        Optional<Funcionario> funcionarioOptional = this.conexaoFuncionario.findById(id);

        if (funcionarioOptional.isPresent()) {
            return funcionarioOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public FuncionarioDTO update(FuncionarioDTO funcionarioDTO, Long id){

        Optional<Funcionario> funcionarioExistenteOptional = this.conexaoFuncionario.findById(id);

        if (funcionarioExistenteOptional.isPresent()){
            Funcionario funcionarioExistente = funcionarioExistenteOptional.get();
            LOGGER.info("Atualizando funcionário ... id:[{}]", funcionarioExistente.getId());
            LOGGER.debug("Payload: {}", funcionarioDTO);
            LOGGER.debug("Funcionário existente: {}", funcionarioExistente);

            funcionarioExistente.setNome(funcionarioDTO.getNome());
            funcionarioExistente.seteMail(funcionarioDTO.geteMail());

            funcionarioExistente = this.conexaoFuncionario.save(funcionarioExistente);
            return FuncionarioDTO.of(funcionarioExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public  void delete(Long id){
        LOGGER.info("Executenado o delete para funcionário de ID: [{}]", id);
        this.conexaoFuncionario.deletePorId(id);
    }
    private void hbEmployeeValidarFuncionario(FuncionarioDTO funcionarioDTO){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","f5a00604-1b67-11ea-978f-2e728ce88125");
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity httpEntity = new HttpEntity(funcionarioDTO,httpHeaders);
        ResponseEntity<EmployeeSalvaDTO> responseEntity = restTemplate.exchange("http://10.2.54.25:9999/api/employees", HttpMethod.POST, httpEntity, EmployeeSalvaDTO.class);
        funcionarioDTO.setUuid(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).getEmployeeUuid()));
        funcionarioDTO.setNome(responseEntity.getBody().getEmployeeName());
    }

    public List<Funcionario> findByFuncionario(){
       return conexaoFuncionario.findByFuncionarioLista();
    }


}




