package br.com.hbsis.funcionario;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncionarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);

    private final IFuncionarioRepository iFuncionarioRepository;

    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository) {
        this.iFuncionarioRepository = iFuncionarioRepository;
    }

    public  FuncionarioDTO save(FuncionarioDTO funcionarioDTO){

        this.validate(funcionarioDTO);

        LOGGER.info("Salvando Funcionário");
        LOGGER.debug("Funcionario: {}",funcionarioDTO);

        Funcionario funcionario = new Funcionario();

        funcionario.setId(funcionarioDTO.getId());
        funcionario.seteMail(funcionarioDTO.geteMail());
        funcionario.setNomeFun(funcionarioDTO.getNomeFun());

        funcionario = this.iFuncionarioRepository.save(funcionario);
        return funcionarioDTO.of(funcionario);
    }

    private void validate(FuncionarioDTO funcionarioDTO){

        LOGGER.info("Validando Funcionário");

        if (funcionarioDTO == null){
            throw new  IllegalArgumentException("FuncionarioDTO não deve ser nulo");
        }
    }
    public  FuncionarioDTO findById(Long id){
        Optional<Funcionario>funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioOptional.isPresent()){
            return  FuncionarioDTO.of(funcionarioOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Funcionario findByFuncionarioId(Long id) {
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioOptional.isPresent()) {
            return funcionarioOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public FuncionarioDTO update(FuncionarioDTO funcionarioDTO, Long id){

        Optional<Funcionario> funcionarioExistenteOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioExistenteOptional.isPresent()){
            Funcionario funcionarioExistente = funcionarioExistenteOptional.get();
            LOGGER.info("Atualizando funcionário ... id:[{}]", funcionarioExistente.getId());
            LOGGER.debug("Payload: {}", funcionarioDTO);
            LOGGER.debug("Funcionário existente: {}", funcionarioExistente);

            funcionarioExistente.setNomeFun(funcionarioDTO.getNomeFun());
            funcionarioExistente.seteMail(funcionarioDTO.geteMail());

            funcionarioExistente = this.iFuncionarioRepository.save(funcionarioExistente);
            return FuncionarioDTO.of(funcionarioExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public  void delete(Long id){
        LOGGER.info("Executenado o delete para funcionário de ID: [{}]", id);

        this.iFuncionarioRepository.deleteById(id);

    }


}



