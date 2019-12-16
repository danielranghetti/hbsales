package br.com.hbsis.fornecedor;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaDTO;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.categoria.ICategoriaRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import sun.security.x509.X500Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {


    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);

    private final IFornecedorRepository iFornecedorRepository;
    private final CategoriaService categoriaService;
    private final ICategoriaRepository iCategoriaRepository;

    @Autowired
    public FornecedorService(IFornecedorRepository iFornecedorRepository, @Lazy CategoriaService categoriaService, ICategoriaRepository iCategoriaRepository) {
        this.iFornecedorRepository = iFornecedorRepository;
        this.categoriaService = categoriaService;
        this.iCategoriaRepository = iCategoriaRepository;
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
        if (!(StringUtils.isNumeric(fornecedorDTO.getCnpj()))){
            throw  new IllegalArgumentException("Cnpj não deve conter letras somente números");
        }

        if (fornecedorDTO.getCnpj().length() != 14) {
            throw new IllegalArgumentException("CNPJ deve conter 14 números");
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
        if (!(StringUtils.isNumeric(fornecedorDTO.getTelefone()))){
            throw new IllegalArgumentException("Telefone deve conter letras apenas números");
        }
        if (fornecedorDTO.getTelefone().length() > 14 || fornecedorDTO.getTelefone().length() < 13) {
            throw new IllegalArgumentException("Telefone deve conter entre 13 e 14 números");
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

    public Fornecedor findByFornecedorCnpj(String cnpj) {
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findByCnpj(cnpj);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }

        throw new IllegalArgumentException(String.format("cnpj %s não existe", cnpj));
    }

    // TODO: 12/12/2019 Remover código que não está sendo usado


    public FornecedorDTO update(FornecedorDTO fornecedorDTO, Long id) {
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);
        // TODO: 12/12/2019 recalcular o código das categorias deste fornecedor quando o fornecedor for atualizado
        this.validate(fornecedorDTO);

        if (fornecedorExistenteOptional.isPresent()) {
            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();
            Fornecedor fornecedor = new Fornecedor();
            List<Categoria> categorias = new ArrayList<>();
            fornecedor = findByFornecedorId(id);

            categorias = iCategoriaRepository.findByFornecedor(fornecedor);

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

            for (Categoria categoria : categorias) {
                categoria.setCodigoCategoria(categoria.getCodigoCategoria().substring(7, 10));
                categoria.setFornecedor(fornecedorExistente);
                categoriaService.update(CategoriaDTO.of(categoria), categoria.getId());
            }
            return FornecedorDTO.of(fornecedorExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para fornecedor de ID: [{}]", id);

        this.iFornecedorRepository.deleteById(id);
    }
    // TODO: 12/12/2019 remover código que não está sendo usado


}

