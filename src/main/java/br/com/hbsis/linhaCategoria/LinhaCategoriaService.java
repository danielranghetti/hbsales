package br.com.hbsis.linhaCategoria;
import br.com.hbsis.categoria.CategoriaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaService categoriaService;

    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaService categoriaService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaService = categoriaService;
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {
        this.validate(linhaCategoriaDTO);
        LOGGER.info("Salvando linha da categoria");
        LOGGER.debug("LinhaCategoria {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();


        String codigoRecebido = linhaCategoriaDTO.getCodLinhaCategoria();

        String codigoCompelto = validarCodigo(codigoRecebido);
        codigoCompelto = codigoCompelto.toUpperCase();


        linhaCategoria.setCodLinhaCategoria(codigoCompelto);
        linhaCategoria.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
        linhaCategoria.setCategoria(categoriaService.findByCategoriaId(linhaCategoriaDTO.getCategoria()));


        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }
    public String validarCodigo(String codigo) {
        String codigoCorrigido = StringUtils.leftPad(codigo, 10, "0");
        return codigoCorrigido;
    }
    public List<LinhaCategoria> saveAll(List<LinhaCategoria> linhaCategorias) throws Exception {
        return iLinhaCategoriaRepository.saveAll(linhaCategorias);
    }
    public LinhaCategoriaDTO findByid(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (((Optional) linhaCategoriaOptional).isPresent()) {
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public LinhaCategoria findByLinhaCategoriaId(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return linhaCategoriaOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public LinhaCategoria findByLinhaCategoriaCodLinhaCategoria(String codLinhaCategoria) {
        Optional<LinhaCategoria> optionalLinhaCategoria = this.iLinhaCategoriaRepository.findByCodLinhaCategoria(codLinhaCategoria);

        if (optionalLinhaCategoria.isPresent()) {
            return optionalLinhaCategoria.get();
        }

        throw new IllegalArgumentException(String.format("codigo  %s don't exist", codLinhaCategoria));
    }
    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {
        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);
            this.validate(linhaCategoriaDTO);
        if (linhaCategoriaExistenteOptional.isPresent()) {
            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Atualizando linha categoria... id:[{}]", linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha Categoria Existente:{}", linhaCategoriaExistente);

            String codigoRecebido = linhaCategoriaDTO.getCodLinhaCategoria();

            String codigoCompelto = validarCodigo(codigoRecebido);
            codigoCompelto = codigoCompelto.toUpperCase();


            linhaCategoriaExistente.setCodLinhaCategoria(codigoCompelto);
            linhaCategoriaExistente.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
            linhaCategoriaExistente.setCategoria(categoriaService.findByCategoriaId(linhaCategoriaDTO.getCategoria()));

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public void delete(Long id) {
        LOGGER.info("Executando delete para linha de categoria de ID:[{}]", id);
        this.iLinhaCategoriaRepository.deleteById(id);
    }
    private void validate(LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Validando linha da categoria");
        if (linhaCategoriaDTO == null) {
            throw new IllegalArgumentException("LinhaCategoriaDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getCodLinhaCategoria())) {
            throw new IllegalArgumentException("Codigo da linha categoria não deve ser nulo");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getNomeLinha())) {
            throw new IllegalArgumentException("Nome linha categoria não dve ser nulo");
        }
        if (StringUtils.isEmpty(Long.toString(linhaCategoriaDTO.getCategoria()))) {
            throw new IllegalArgumentException("categoria da linha categoria não cadastrada");
        }
    }


}


