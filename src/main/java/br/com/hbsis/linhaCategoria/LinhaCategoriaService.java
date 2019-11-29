package br.com.hbsis.linhaCategoria;


import br.com.hbsis.categoria.CategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private static ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private static CategoriaService categoriaService;

    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaService categoriaService) {
        this.iLinhaCategoriaRepository =iLinhaCategoriaRepository;
        this.categoriaService = categoriaService;
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO){
        //this.validate(linhaCategoriaDTO);
    LOGGER.info("Salvando linha da categoria");
    LOGGER.debug("LinhaCategoria {}",linhaCategoriaDTO);

    LinhaCategoria linhaCategoria = new LinhaCategoria();
    linhaCategoria.setCodLinhaCategoria(linhaCategoriaDTO.getCodLinhaCategoria());
    linhaCategoria.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
    linhaCategoria.setCategoria(categoriaService.findByCategoriaId(linhaCategoriaDTO.getCategoria()));




    linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);
    return  LinhaCategoriaDTO.of(linhaCategoria);
     }
     public List<LinhaCategoria> saveAll(List<LinhaCategoria> linhaCategorias) throws Exception{
        return iLinhaCategoriaRepository.saveAll(linhaCategorias);
     }

    public  LinhaCategoriaDTO findByid(Long id){
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (((Optional) linhaCategoriaOptional).isPresent()){
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id){
        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaExistenteOptional.isPresent()){
            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Atualizando linha categoria... id:[{}]",linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha Categoria Existente:{}", linhaCategoriaExistente);

            linhaCategoriaExistente.setCodLinhaCategoria(linhaCategoriaDTO.getCodLinhaCategoria());
            linhaCategoriaExistente.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
            linhaCategoriaExistente.setCategoria(categoriaService.findByCategoriaId(linhaCategoriaDTO.getCategoria()));

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
            }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public  void  delete(Long id){
        LOGGER.info("Executando delete para linha de categoria de ID:[{}]",id);
        this.iLinhaCategoriaRepository.deleteById(id);
    }



}
