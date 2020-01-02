package br.com.hbsis.linhaCategoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConexaoLinhaCategoria {
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;

    @Autowired
    public ConexaoLinhaCategoria(ILinhaCategoriaRepository iLinhaCategoriaRepository) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
    }

    public boolean existsByCodLinhaCategoria(String codLinhaCategoria) {
        return iLinhaCategoriaRepository.existsByCodLinhaCategoria(codLinhaCategoria);
    }

    public Optional<LinhaCategoria> findByCodLinhaCategoria(String codLinhaCategoria) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodLinhaCategoria(codLinhaCategoria);
        if (linhaCategoriaOptional.isPresent()) {
            return linhaCategoriaOptional;
        }
        throw new IllegalArgumentException(String.format("Código linha Categoria %s não existe", codLinhaCategoria));
    }

    public List<LinhaCategoria> saveAll(List<LinhaCategoria> linhaCategorias) throws Exception {
        return iLinhaCategoriaRepository.saveAll(linhaCategorias);
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

    public Optional<LinhaCategoria> findById(Long id){
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);
        if (linhaCategoriaOptional.isPresent()){
            return linhaCategoriaOptional;
        }
        throw  new IllegalArgumentException(String.format("ID %s não existe",id));
    }

    public void deletePorId(Long id) {
        this.iLinhaCategoriaRepository.deleteById(id);
    }

    public LinhaCategoria save(LinhaCategoria linhaCategoria) {
        try {
            linhaCategoria = iLinhaCategoriaRepository.save(linhaCategoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linhaCategoria;
    }

    public  List<LinhaCategoria> findAll(){
        List<LinhaCategoria> linhaCategoriaList = new ArrayList<>();
        try {
            linhaCategoriaList = this.iLinhaCategoriaRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return linhaCategoriaList;
    }

}
