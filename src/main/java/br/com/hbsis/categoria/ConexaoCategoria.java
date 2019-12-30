package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConexaoCategoria {

    private final ICategoriaRepository iCategoriaRepository;

    @Autowired
    public ConexaoCategoria(ICategoriaRepository iCategoriaRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
    }

    public boolean existsByNomeCategoria(String nomeCategoria){
        return iCategoriaRepository.existsByNomeCategoria(nomeCategoria);
    }

    public  boolean existsCategoriaProdutoByFornecedorId(Long id){
        return iCategoriaRepository.existsCategoriaProdutoByFornecedorId(id);
    }

    public boolean existsByCodigoCategoria (String codigoCategoria){
        return iCategoriaRepository.existsByCodigoCategoria(codigoCategoria);
    }

    public Optional<Categoria> findByCodigoCategoria(String codigoCategoria) {
        Optional<Categoria> optionalCategoria = this.iCategoriaRepository.findByCodigoCategoria(codigoCategoria);

        if (optionalCategoria.isPresent()) {
            return optionalCategoria;
        }
        throw new IllegalArgumentException(String.format("Codigo categoria %s não existe", codigoCategoria));
    }

    public Categoria findByCodigoCategoria1(String codigoCategoria) {
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

    public Optional<Categoria> findById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return categoriaOptional;
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id){
        this.iCategoriaRepository.deleteById(id);
    }

    public Categoria save(Categoria categoria){
        try {
            categoria = iCategoriaRepository.save(categoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoria;
    }

    public List<Categoria> saveAll(List<Categoria> categoria) throws Exception {

        return iCategoriaRepository.saveAll(categoria);
    }

    public List<Categoria> findAll(){
        List<Categoria> categoriaList = new ArrayList<>();
        try {
            categoriaList = iCategoriaRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoriaList;

    }
     public List<Categoria>  findByFornecedor(Fornecedor fornecedor){
        List<Categoria> categorias = new ArrayList<>();
        try {
            categorias = iCategoriaRepository.findByFornecedor(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
     }
}
