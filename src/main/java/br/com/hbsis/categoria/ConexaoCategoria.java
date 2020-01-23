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


    public boolean existsByCodigoCategoria(String codigoCategoria) {
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

    public void deletePorId(Long id) {
        this.iCategoriaRepository.deleteById(id);
    }

    public Categoria save(Categoria categoria) {
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

    public List<Categoria> findAll() {
        List<Categoria> categoriaList = new ArrayList<>();
        try {
            categoriaList = iCategoriaRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoriaList;

    }

    public List<Categoria> findByFornecedor(Fornecedor fornecedor) {
        List<Categoria> categorias = new ArrayList<>();
        try {
            categorias = iCategoriaRepository.findByFornecedor(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public void updateCodigoCategoriaPorCnpj(Fornecedor fornecedor) {
        try {
            List<Categoria> categoriaList = iCategoriaRepository.findByFornecedor(fornecedor);
            for (Categoria categoria : categoriaList) {
                categoria.setNomeCategoria(categoria.getNomeCategoria());
                categoria.setFornecedor(fornecedor);
                String cnpj = categoria.getFornecedor().getCnpj();
                String ultDig = cnpj.substring(cnpj.length() - 4);
                categoria.setCodigoCategoria(categoria.getCodigoCategoria().replace(categoria.getCodigoCategoria().substring(3, 7), ultDig));
                this.iCategoriaRepository.save(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

