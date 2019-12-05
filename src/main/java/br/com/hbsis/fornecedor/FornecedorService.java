package br.com.hbsis.fornecedor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FornecedorService {


    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);

    private final IFornecedorRepository iFornecedorRepository;

    @Autowired
    public FornecedorService(IFornecedorRepository iFornecedorRepository) {
        this.iFornecedorRepository = iFornecedorRepository;
    }

    public FornecedorDTO save(FornecedorDTO fornecedorDTO) {

        this.validate(fornecedorDTO);

        LOGGER.info("Salvando fornecedor");
        LOGGER.debug("Fornecedor: {}", fornecedorDTO);

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelefone(fornecedorDTO.getTelefone());
        fornecedor.seteMail(fornecedorDTO.geteMail());

        fornecedor = this.iFornecedorRepository.save(fornecedor);
        return FornecedorDTO.of(fornecedor);
    }

    private void validate(FornecedorDTO fornecedorDTO) {
        LOGGER.info("Validando Fornecedor");
        if (fornecedorDTO == null) {
            throw new IllegalArgumentException("FornecedorDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(fornecedorDTO.getRazaoSocial())) {
            throw new IllegalArgumentException("Razão Social não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getCnpj())) {
            throw new IllegalArgumentException("CNPJ não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getNomeFantasia())) {
            throw new IllegalArgumentException("Nome fantasia não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getEndereco())) {
            throw new IllegalArgumentException("Endereço não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getTelefone())) {
            throw new IllegalArgumentException("Telefone não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedorDTO.geteMail())) {
            throw new IllegalArgumentException("E-mail não deve ser nulo/vazio");
        }

    }

    public FornecedorDTO findById(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FornecedorDTO.of(fornecedorOptional.get());
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
    public Fornecedor findFornecedorEntityById(Long id){

        Optional<Fornecedor> fornecedor = this.iFornecedorRepository.findById(id);
        if(fornecedor.isPresent()){
            return fornecedor.get();
        }
        throw new IllegalArgumentException(String.format("Fornecedor ID %s não existe.", id));
    }

    public FornecedorDTO update(FornecedorDTO fornecedorDTO, Long id) {
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);

        if (fornecedorExistenteOptional.isPresent()) {
            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

            LOGGER.info("Atualizando fornecedor... id: [{}]", fornecedorExistente.getId());
            LOGGER.debug("Payload: {}", fornecedorDTO);
            LOGGER.debug("Fornecedor Existente: {}", fornecedorExistente);


            fornecedorExistente.setRazaoSocial(fornecedorDTO.getRazaoSocial());
            fornecedorExistente.setCnpj(fornecedorDTO.getCnpj());
            fornecedorExistente.setNomeFantasia(fornecedorDTO.getNomeFantasia());
            fornecedorExistente.setEndereco(fornecedorDTO.getEndereco());
            fornecedorExistente.setTelefone(fornecedorDTO.getTelefone());
            fornecedorExistente.seteMail(fornecedorDTO.geteMail());

            fornecedorExistente = this.iFornecedorRepository.save(fornecedorExistente);

            return FornecedorDTO.of(fornecedorExistente);
        }


        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para fornecedor de ID: [{}]", id);

        this.iFornecedorRepository.deleteById(id);
    }
}

