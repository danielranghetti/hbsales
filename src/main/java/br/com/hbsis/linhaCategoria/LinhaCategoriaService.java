package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoria.ConexaoCategoria;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ConexaoCategoria conexaoCategoria;
    private final ConexaoLinhaCategoria conexaoLinhaCategoria;

    public LinhaCategoriaService(ConexaoCategoria conexaoCategoria, ConexaoLinhaCategoria conexaoLinhaCategoria) {
        this.conexaoCategoria = conexaoCategoria;
        this.conexaoLinhaCategoria = conexaoLinhaCategoria;
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
        linhaCategoria.setCategoria(conexaoCategoria.findByCategoriaId(linhaCategoriaDTO.getCategoria()));


        linhaCategoria = this.conexaoLinhaCategoria.save(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public String validarCodigo(String codigo) {
        String codigoCorrigido = StringUtils.leftPad(codigo, 10, "0");
        return codigoCorrigido;
    }

    public LinhaCategoriaDTO findByid(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.conexaoLinhaCategoria.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {
        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.conexaoLinhaCategoria.findById(id);
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
            linhaCategoriaExistente.setCategoria(conexaoCategoria.findByCategoriaId(linhaCategoriaDTO.getCategoria()));

            linhaCategoriaExistente = this.conexaoLinhaCategoria.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para linha de categoria de ID:[{}]", id);
        this.conexaoLinhaCategoria.deletePorId(id);

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


