package br.com.hbsis.funcionario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConexaoFuncionario {

    private final IFuncionarioRepository iFuncionarioRepository;

    @Autowired
    public ConexaoFuncionario(IFuncionarioRepository iFuncionarioRepository) {
        this.iFuncionarioRepository = iFuncionarioRepository;
    }
    public  Funcionario save(Funcionario funcionario){
        try {
            funcionario = iFuncionarioRepository.save(funcionario);
        }catch (Exception e){
            e.printStackTrace();
        }
        return funcionario;
    }
    public void deletePorId(Long id){
        this.iFuncionarioRepository.deleteById(id);
    }
    public Optional<Funcionario> findById(Long id){
        Optional<Funcionario> funcionarioOptional = iFuncionarioRepository.findById(id);
        if (funcionarioOptional.isPresent()){
            return funcionarioOptional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe",id));
    }
    public List<Funcionario> findByFuncionarioLista(){
        List<Funcionario> funcionarioList = new ArrayList<>();
        try {
            funcionarioList = iFuncionarioRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return funcionarioList;
    }



}
