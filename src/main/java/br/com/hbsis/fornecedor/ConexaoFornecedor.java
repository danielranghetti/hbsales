package br.com.hbsis.fornecedor;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConexaoFornecedor {
    private final IFornecedorRepository iFornecedorRepository;

    public ConexaoFornecedor(IFornecedorRepository iFornecedorRepository) {
        this.iFornecedorRepository = iFornecedorRepository;
    }

    public FornecedorDTO findByFornecedorDTOId(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public Optional<Fornecedor> findById(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional;
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Fornecedor findByFornecedorId(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Fornecedor findByFornecedorCnpj(String cnpj) {
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findByCnpj(cnpj);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }

        throw new IllegalArgumentException(String.format("cnpj %s não existe", cnpj));
    }

    public Fornecedor save(Fornecedor fornecedor){
        try {
            fornecedor = iFornecedorRepository.save(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fornecedor;
    }
    public Fornecedor update(Fornecedor fornecedor){
        try {
            fornecedor = iFornecedorRepository.save(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fornecedor;

    }


    public void delete(Long id){
        this.iFornecedorRepository.deleteById(id);
    }

    public boolean existsById(Long id){
        return iFornecedorRepository.existsById(id);
    }

    public  boolean existsByCnpj (String cnpj){
        return iFornecedorRepository.existsByCnpj(cnpj);
    }
}
