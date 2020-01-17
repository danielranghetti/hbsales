package br.com.hbsis.funcionario;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioRest.class);

    private final FuncionarioService funcionarioService;

    @Autowired
    public FuncionarioRest(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }
    @GetMapping("/{id}")
    public   FuncionarioDTO find(@PathVariable("id") Long id){
        LOGGER.info("Recebendo fin by ID: [{}]", id);
        return this.funcionarioService.findById(id);
    }
    @PostMapping("/save")
    public FuncionarioDTO save(@RequestBody FuncionarioDTO funcionarioDTO){
        LOGGER.info("Recebendo solicitação de persistência de funcionário ....");
        LOGGER.debug("Payload: {}", funcionarioDTO);

        return this.funcionarioService.save(funcionarioDTO);
    }
    @PutMapping("/editar/{id}")

    public  FuncionarioDTO update(@PathVariable("id") Long id, @RequestBody FuncionarioDTO funcionarioDTO){

        LOGGER.info("recebendo atualização para funcionário de ID: {}",id);
        LOGGER.debug("payload: {}", funcionarioDTO);
        return this.funcionarioService.update(funcionarioDTO, id);
    }
    @DeleteMapping("/delete/{id}")
    public  void delete(@PathVariable("id") Long id){
        LOGGER.info("Recebendo delete para funcionário de ID: {}",id);

        this.funcionarioService.delete(id);
    }
    @GetMapping("findFuncionarios")
    public List<Funcionario> finddByFuncionario() {
        return this.funcionarioService.findByFuncionario();
    }

}
