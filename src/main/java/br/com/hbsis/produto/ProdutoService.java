package br.com.hbsis.produto;

import br.com.hbsis.linhaCategoria.ILinhaCategoriaRepository;
import br.com.hbsis.linhaCategoria.LinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoriaDTO;
import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
import com.opencsv.*;

import jdk.internal.util.xml.impl.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.parse;


@Service
public class ProdutoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private static ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private static IProdutoRepository iProdutoRepository;
    private static LinhaCategoriaService linhaCategoriaService;
    private static LinhaCategoriaDTO linhaCategoriaDTO;

    public ProdutoService(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService, ILinhaCategoriaRepository iLinhaCategoriaRepository) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;

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

    public void findAll(HttpServletResponse response) throws Exception {

        String arquivo = "produtos.cv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String titulo[] = {"id_produto", "id_linha_categoria", "cod_produto", "nome", "preco", "uni_caixa", "peso_uni", "validade"};
        icsvWriter.writeNext(titulo);

        for (Produto linha : iProdutoRepository.findAll()) {
            icsvWriter.writeNext(new String[]{linha.getId().toString(), linha.getLinhaCategoria().getId().toString(), String.valueOf(linha.getCodProduto()),
                    linha.getNome(), Double.toString(linha.getPreco()), String.valueOf(linha.getUniCaixa()), Double.toString(linha.getPesoUni()), linha.getValidade().toString()
            });
        }

    }

    //faz a importação
    public List<Produto> reaAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1).build();

        List<String[]> linhaString = csvReader.readAll();
        List<Produto> resultadoLeitura = new ArrayList<>();

        for (String[] linha : linhaString) {
            try {
                String[] dados = linha[0].replaceAll("\"", "").split(";");

                Produto produto = new Produto();
                produto.setId(Long.parseLong(dados[0]));
                produto.setCodProduto(Integer.parseInt(dados[2]));
                produto.setNome(dados[3]);
                produto.setPreco(Double.parseDouble(dados[4]));
                produto.setUniCaixa(Integer.parseInt(dados[5]));
                produto.setPesoUni(Double.parseDouble(dados[6]));
                produto.setValidade(LocalDate.parse(dados[7]));
                produto.setLinhaCategoria(linhaCategoriaService.findByLinhaCategoriaId(Long.parseLong(dados[1])));


                resultadoLeitura.add(produto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iProdutoRepository.saveAll(resultadoLeitura);

    }
}













