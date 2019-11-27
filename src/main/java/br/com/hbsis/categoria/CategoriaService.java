package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.Optional;

@Service
public class CategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);

    private final ICategoriaRepository iCategoriaRepository;
    private final IFornecedorRepository iFornecedorRepository;
    private final FornecedorService fornecedorService;

    public CategoriaService(ICategoriaRepository iCategoriaRepository, IFornecedorRepository iFornecedorRepository, FornecedorService fornecedorService) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.iFornecedorRepository = iFornecedorRepository;
        this.fornecedorService = fornecedorService;
    }



    public CategoriaDTO save(CategoriaDTO categoriaDTO){
        this.validate(categoriaDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaDTO);

        Categoria categoria = new Categoria();
        categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
        categoria.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getFornecedor()));
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());

        categoria = this.iCategoriaRepository.save(categoria);
        return CategoriaDTO.of(categoria);
    }
    private void validate(CategoriaDTO categoriaDTO){
        LOGGER.info("Validando categoria");
        if (categoriaDTO==null){
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(Long.toString(categoriaDTO.getFornecedor()))){
            throw new IllegalArgumentException("Fornecedo categoria não cadastrado");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())){
            throw new IllegalArgumentException("Nome categoria não deve ser nulo");
        }
        if (StringUtils.isEmpty(Long.toString(categoriaDTO.getCodigoCategoria()))){
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
    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptional = this.iCategoriaRepository.findById(id);

        if (categoriaExistenteOptional.isPresent()) {
            Categoria categoriaExistente = categoriaExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaExistente);

            categoriaExistente.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
            categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());


            categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

            return CategoriaDTO.of(categoriaExistente);
        }
        throw  new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria de ID: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }


    }
