package br.com.hbsis.produto;

import br.com.hbsis.linhaCategoria.ConexaoLinhaCategoria;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final ConexaoProduto conexaoProduto;
    private final ConexaoLinhaCategoria conexaoLinhaCategoria;



    public ProdutoService(ConexaoProduto conexaoProduto, ConexaoLinhaCategoria conexaoLinhaCategoria) {
        this.conexaoProduto = conexaoProduto;
        this.conexaoLinhaCategoria = conexaoLinhaCategoria;

    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {
        this.validate(produtoDTO);
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto {}", produtoDTO);

        Produto produto = new Produto();

        String codigorecebido = produtoDTO.getCodProduto();
        String codigoComZero = validarCodigo(codigorecebido);
        String codFinal = codigoComZero.toUpperCase();

        produto.setCodProduto(codFinal);
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setUniCaixa(produtoDTO.getUniCaixa());
        produto.setPesoUni(produtoDTO.getPesoUni());
        produto.setValidade(produtoDTO.getValidade());
        produto.setUnidadeMedida(produtoDTO.getUnidadeMedida());
        produto.setLinhaCategoria(conexaoLinhaCategoria.findByLinhaCategoriaId(produtoDTO.getLinhaCategoria()));

        produto = this.conexaoProduto.save(produto);
        return ProdutoDTO.of(produto);
    }

    public String validarCodigo(String codigo) {
        String codigoProcessador = StringUtils.leftPad(codigo, 10, "0");
        return codigoProcessador;
    }

    public ProdutoDTO finById(Long id) {
        Optional<Produto> produtoOptional = this.conexaoProduto.findById(id);

        if (produtoOptional.isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }
        throw new IllegalArgumentException(String.format("esse  %s não existe", id));

    }



    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoExistenteOptional = this.conexaoProduto.findById(id);

        this.validate(produtoDTO);

        if (produtoExistenteOptional.isPresent()) {
            Produto produtoExistente = produtoExistenteOptional.get();
            LOGGER.info("Atualizando produto... id:[{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("produto Existente:{}", produtoExistente);

            String codigorecebido = produtoDTO.getCodProduto();
            String codigoComZero = validarCodigo(codigorecebido);
            String codFinal = codigoComZero.toUpperCase();


            produtoExistente.setCodProduto(codFinal);
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setUniCaixa(produtoDTO.getUniCaixa());
            produtoExistente.setPesoUni(produtoDTO.getPesoUni());
            produtoExistente.setValidade(produtoDTO.getValidade());
            produtoExistente.setLinhaCategoria(conexaoLinhaCategoria.findByLinhaCategoriaId(produtoDTO.getLinhaCategoria()));


            produtoExistente = conexaoProduto.save(produtoExistente);
            return ProdutoDTO.of(produtoExistente);

        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria de ID: [{}]", id);

        conexaoProduto.deletePorId(id);
    }

    private void validate(ProdutoDTO produtoDTO) {

        if (produtoDTO == null) {
            throw new IllegalArgumentException("ProditoDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(produtoDTO.getCodProduto())) {
            throw new IllegalArgumentException("Código do produto não pode ser nulo");
        }
        if (StringUtils.isEmpty(produtoDTO.getNome())) {
            throw new IllegalArgumentException("O nome do produto nao deve ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPesoUni()))) {
            throw new IllegalArgumentException("O peso unitário não deve ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getPreco()))) {
            throw new IllegalArgumentException("O preço não deve ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getUniCaixa()))) {
            throw new IllegalArgumentException("A unidade por caixa não deve ser nula");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getValidade()))) {
            throw new IllegalArgumentException("A data de validade do produto não deve ser nula");
        }
        if (StringUtils.isEmpty(String.valueOf(produtoDTO.getLinhaCategoria()))) {
            throw new IllegalArgumentException("Linha do produto não cadrastada");
        }
        if (StringUtils.isEmpty(produtoDTO.getUnidadeMedida())) {
            throw new IllegalArgumentException("Unidade de medida não pode ser nula");
        }
        switch (produtoDTO.getUnidadeMedida()) {
            case "mg":
            case "g":
            case "Kg":
                break;
            default:
                throw new IllegalArgumentException("Unidade de medida não permitida");
        }
    }
}