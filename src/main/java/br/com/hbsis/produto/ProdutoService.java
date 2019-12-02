package br.com.hbsis.produto;

import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
import com.sun.javafx.binding.StringFormatter;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;


@Service
public class ProdutoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private static IProdutoRepository iProdutoRepository;
    private static LinhaCategoriaService linhaCategoriaService;

    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;

    }

    public List<Produto> saveAll(List<Produto> produto) throws Exception {

        return iProdutoRepository.saveAll(produto);
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {
        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto {}", produtoDTO);

        Produto produto = new Produto();
        produto.setCodProduto(produtoDTO.getCodProduto());
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());
        produto.setUniCaixa(produtoDTO.getUniCaixa());
        produto.setPesoUni(produtoDTO.getPesoUni());
        produto.setValidade(produtoDTO.getValidade());
        produto.setLinhaCategoria(linhaCategoriaService.findByLinhaCategoriaId(produtoDTO.getLinhaCategoria()));

        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    public ProdutoDTO finById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (((Optional) produtoOptional).isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }
        throw new IllegalArgumentException(String.format("esse %s não existe", id));

    }


    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

        if (produtoExistenteOptional.isPresent()) {
            Produto produtoExistente = produtoExistenteOptional.get();
            LOGGER.info("Atualizando produto... id:[{}]", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("produto Existente:{}", produtoExistente);

            produtoExistente.setCodProduto(produtoDTO.getCodProduto());
            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setUniCaixa(produtoDTO.getUniCaixa());
            produtoExistente.setPesoUni(produtoDTO.getPesoUni());
            produtoExistente.setValidade(produtoDTO.getValidade());


            produtoExistente = iProdutoRepository.save(produtoExistente);
            return ProdutoDTO.of(produtoExistente);

        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }
    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria de ID: [{}]", id);

        iProdutoRepository.deleteById(id);
    }
    // exportação

    public void findAll(HttpServletResponse response) throws Exception{

        String arquivo = "produtos.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+arquivo + "\"");
        PrintWriter Writer =response.getWriter();


    }
}
