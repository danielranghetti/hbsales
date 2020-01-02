package br.com.hbsis.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConexaoProduto {
    private final IProdutoRepository iProdutoRepository;

    @Autowired
    public ConexaoProduto(IProdutoRepository iProdutoRepository) {
        this.iProdutoRepository = iProdutoRepository;
    }

    public boolean existsByCodProduto(String codProduto) {
        return iProdutoRepository.existsByCodProduto(codProduto);
    }

    public Produto findByCodProduto(String codProduto) {
        Optional<Produto> produtoOptional = iProdutoRepository.findByCodProduto(codProduto);
        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }
        throw new IllegalArgumentException(String.format("Código do Produto %s não existe", codProduto));
    }

    public Optional<Produto> findByCodProdutoOptional(String codProduto) {
        Optional<Produto> produtoOptional = iProdutoRepository.findByCodProduto(codProduto);
        if (produtoOptional.isPresent()) {
            return produtoOptional;
        }
        throw new IllegalArgumentException(String.format("Código do Produto %s não existe", codProduto));
    }

    public Optional<Produto> findById(Long id) {
        Optional<Produto> optionalProduto = iProdutoRepository.findById(id);
        if (optionalProduto.isPresent()) {
            return optionalProduto;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Produto> saveAll(List<Produto> produto) throws Exception {

        return iProdutoRepository.saveAll(produto);
    }

    public Produto save(Produto produto) {
        try {
            produto = iProdutoRepository.save(produto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produto;
    }

    public void deletePorId(Long id) {
        this.iProdutoRepository.deleteById(id);
    }

    public List<Produto> findAll() {
        List<Produto> produtoList = new ArrayList<>();
        try {
            produtoList = iProdutoRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtoList;
    }

    public Produto findByProdutoId(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


}
