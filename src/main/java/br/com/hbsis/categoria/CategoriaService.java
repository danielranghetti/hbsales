package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.ConexaoFornecedor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);

    private final ICategoriaRepository iCategoriaRepository;
    private final ConexaoFornecedor conexaoFornecedor;


    public CategoriaService(ICategoriaRepository iCategoriaRepository, ConexaoFornecedor conexaoFornecedor) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.conexaoFornecedor = conexaoFornecedor;

    }

    public List<Categoria> saveAll(List<Categoria> categoria) throws Exception {

        return iCategoriaRepository.saveAll(categoria);
    }

    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        this.validate(categoriaDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaDTO);

        Categoria categoria = new Categoria();

        String incial = "CAT";

        categoria.setFornecedor(conexaoFornecedor.findByFornecedorId(categoriaDTO.getFornecedor()));
        String cnpj = categoria.getFornecedor().getCnpj();
        String ultDig = cnpj.substring(cnpj.length() - 4);

        String codigo = categoriaDTO.getCodigoCategoria();
        String codigoComZero = codigoValidar(codigo);

        categoria.setCodigoCategoria(incial + ultDig + codigoComZero);
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());

        categoria = this.iCategoriaRepository.save(categoria);
        return CategoriaDTO.of(categoria);
    }

    private String codigoValidar(String codigo) {
        String codigoProcessador = StringUtils.leftPad(codigo, 3, "0");
        return codigoProcessador;
    }

    private void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("Validando categoria");
        if (categoriaDTO == null) {
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(Long.toString(categoriaDTO.getFornecedor()))) {
            throw new IllegalArgumentException("Fornecedo categoria não cadastrado");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome categoria não deve ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getCodigoCategoria())) {
            throw new IllegalArgumentException("Codigo categoria não deve ser nulo");
        }
    }

    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (((Optional) categoriaOptional).isPresent()) {
            return CategoriaDTO.of(categoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Categoria findByCodigoCategoria(String codigoCategoria) {
        Optional<Categoria> optionalCategoria = this.iCategoriaRepository.findByCodigoCategoria(codigoCategoria);

        if (optionalCategoria.isPresent()) {
            return optionalCategoria.get();
        }
        throw new IllegalArgumentException(String.format("Codigo categoria %s não existe", codigoCategoria));
    }

    public Categoria findByCategoriaId(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if ((categoriaOptional).isPresent()) {
            return categoriaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptional = this.iCategoriaRepository.findById(id);

        if (categoriaExistenteOptional.isPresent()) {
            Categoria categoriaExistente = categoriaExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaExistente);

            String incial = "CAT";

            categoriaExistente.setFornecedor(conexaoFornecedor.findByFornecedorId(categoriaDTO.getFornecedor()));
            String cnpj = categoriaExistente.getFornecedor().getCnpj();
            String ultDig = cnpj.substring(cnpj.length() - 4);

            String codigo = categoriaDTO.getCodigoCategoria();
            String codigoComZero = codigoValidar(codigo);
            codigoComZero = codigoComZero.toUpperCase();

            categoriaExistente.setCodigoCategoria(incial + ultDig + codigoComZero);
            categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());

            categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

            return CategoriaDTO.of(categoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria de ID: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }


}
