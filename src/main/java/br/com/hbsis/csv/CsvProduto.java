package br.com.hbsis.csv;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.ConexaoCategoria;
import br.com.hbsis.ferramentas.MascaraCnpj;
import br.com.hbsis.fornecedor.ConexaoFornecedor;
import br.com.hbsis.linhaCategoria.ILinhaCategoriaRepository;
import br.com.hbsis.linhaCategoria.LinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
import br.com.hbsis.produto.IProdutoRepository;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CsvProduto {
    private static final Logger LOGGER = LoggerFactory.getLogger(Produto.class);
    private final IProdutoRepository iProdutoRepository;
    private final LinhaCategoriaService linhaCategoriaService;
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final ConexaoFornecedor conexaoFornecedor;
    private final ConexaoCategoria conexaoCategoria;
    private final ProdutoService produtoService;


    @Autowired
    public CsvProduto(IProdutoRepository iProdutoRepository, LinhaCategoriaService linhaCategoriaService, ILinhaCategoriaRepository iLinhaCategoriaRepository, ConexaoFornecedor conexaoFornecedor, ConexaoCategoria conexaoCategoria, ProdutoService produtoService) {
        this.iProdutoRepository = iProdutoRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.conexaoFornecedor = conexaoFornecedor;
        this.conexaoCategoria = conexaoCategoria;
        this.produtoService = produtoService;

    }

    public void csvToProdutoExport(HttpServletResponse response) throws Exception {
        MascaraCnpj mascaraCnpj = new MascaraCnpj();

        String arquivo = " csv_produtos.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] titulo = {"código", "nome", "preço", "quantidade de unidades por caixa", "peso ", "validade", " código da linha de categoria", "nome da Linha de Categoria",
                "código da Categoria", " nome da Categoria", "CNPJ", "razão social", ""};
        icsvWriter.writeNext(titulo);

        for (Produto linha : iProdutoRepository.findAll())
            icsvWriter.writeNext(new String[]{linha.getCodProduto(), linha.getNome(), "R$" + (linha.getPreco()), String.valueOf(linha.getUniCaixa()), linha.getPesoUni() + linha.getUnidadeMedida(),
                    linha.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyy")), linha.getLinhaCategoria().getCodLinhaCategoria(), linha.getLinhaCategoria().getNomeLinha(), linha.getLinhaCategoria().getCategoria().getCodigoCategoria(),
                    linha.getLinhaCategoria().getCategoria().getNomeCategoria(), mascaraCnpj.formatCnpj(linha.getLinhaCategoria().getCategoria().getFornecedor().getCnpj()), linha.getLinhaCategoria().getCategoria().getFornecedor().getRazaoSocial()
            });

    }

    public void csvToProdutoFornecedorExport(HttpServletResponse response) throws Exception {

        String arquivo = " csv_produtos-fornecedor.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] titulo = {"código", "nome", "preço", "quantidade de unidades por caixa", "peso ", "validade", " código da linha de categoria", "nome da Linha de Categoria",
                "código da Categoria", "nome categoria"};
        icsvWriter.writeNext(titulo);

        for (Produto linha : iProdutoRepository.findAll()) {
            icsvWriter.writeNext(new String[]{linha.getCodProduto(), linha.getNome(), "R$" + (linha.getPreco()), String.valueOf(linha.getUniCaixa()), linha.getPesoUni() + linha.getUnidadeMedida(),
                   linha.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyy")), linha.getLinhaCategoria().getCodLinhaCategoria(), linha.getLinhaCategoria().getNomeLinha(), linha.getLinhaCategoria().getCategoria().getCodigoCategoria(),
                    linha.getLinhaCategoria().getCategoria().getNomeCategoria()
            });
        }

    }

    private List<Produto> csvToProduto(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1).build();

        List<String[]> linhaString = csvReader.readAll();
        List<Produto> resultadoLeitura = new ArrayList<>();

        for (String[] linha : linhaString) {
            try {
                String[] dados = linha[0].replaceAll("\"", "").split(";");

                Produto produto = new Produto();
                LinhaCategoria linhaCategoria;

                String codProduto = dados[0];
                String nomeProduto = dados[1];
                double preco = Double.parseDouble(dados[2].trim().replace(',', '.').replaceAll("[A-Z$]", ""));
                int uniCaixa = Integer.parseInt(dados[3]);
                double pesoUni = Double.parseDouble(dados[4].replaceAll("[A-Za-z]", ""));
                String unidadeMedida = dados[4].replaceAll("[0-9]", "");
                String codLinhaCategoria = dados[6];
                String data = dados[5];
                int dia = Integer.parseInt(data.substring(0, 2));
                int mes = Integer.parseInt(data.substring(3, 5));
                int ano = Integer.parseInt(data.substring(6, 10));
                LocalDate datavalidade = LocalDate.of(ano, mes, dia);


                if (iProdutoRepository.existsByCodProduto(codProduto)) {
                    LOGGER.info("Produto: {}", codProduto + " já existe");

                } else if (!iProdutoRepository.existsByCodProduto(dados[6])) {

                    produto.setCodProduto((codProduto));
                    produto.setNome(nomeProduto);
                    produto.setPreco(preco);
                    produto.setUniCaixa(uniCaixa);
                    produto.setPesoUni(pesoUni);
                    produto.setUnidadeMedida(unidadeMedida);
                    produto.setValidade(datavalidade);
                    linhaCategoria = linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(codLinhaCategoria);
                    produto.setLinhaCategoria(linhaCategoria);
                    resultadoLeitura.add(produto);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iProdutoRepository.saveAll(resultadoLeitura);

    }

    public void importFromCsvProduto(MultipartFile file) throws Exception {
        List<Produto> produtos = this.csvToProduto(file);
        produtoService.saveAll(produtos);
    }

    public void csvToProdutoFornecedor(Long id, MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1).build();

        List<String[]> linhaString = csvReader.readAll();

        for (String[] linha : linhaString) {


            try {
                String[] resultado = linha[0].replaceAll("\"", "").split(";");

                Produto produto = new Produto();
                LinhaCategoria linhaCategoria = new LinhaCategoria();
                Categoria categoria = new Categoria();

                String codProduto = resultado[0];
                String nomeProduto = resultado[1];
                double preco = Double.parseDouble(resultado[2].trim().replace(',', '.').replaceAll("[A-Z$]", ""));
                int uniCaixa = Integer.parseInt(resultado[3]);
                double peso = Double.parseDouble(resultado[4].replaceAll("[A-Za-z]", ""));
                String unimedida = resultado[4].replaceAll("[0-9.]", "");

                String data = resultado[5];
                int dia = Integer.parseInt(data.substring(0, 2));
                int mes = Integer.parseInt(data.substring(3, 5));
                int ano = Integer.parseInt(data.substring(6, 10));
                LocalDate datavalidade = LocalDate.of(ano, mes, dia);

                String codLinhaCategoria = resultado[6];
                String nomeLinha = resultado[7];
                String codigocategoria = resultado[8];
                String nomeCategoria = resultado[9];
                String cnpj = resultado[10].replaceAll("[^0-9]","");




                if (conexaoFornecedor.existsById(id) && conexaoFornecedor.findByFornecedorCnpj(cnpj).getId().equals(id)) {
                    if (!conexaoCategoria.existsByCodigoCategoria(codigocategoria)) {
        
                        categoria.setNomeCategoria(nomeCategoria);
                        categoria.setCodigoCategoria(codigocategoria);
                        categoria.setFornecedor(conexaoFornecedor.findByFornecedorId(id));
                        conexaoCategoria.save(categoria);

                    } else if (conexaoCategoria.existsByCodigoCategoria(codigocategoria)) {
                        categoria = conexaoCategoria.findByCodigoCategoria1(codigocategoria);
                        Optional<Categoria> categoriaOptional = this.conexaoCategoria.findByCodigoCategoria(codigocategoria);

                        if (categoriaOptional.isPresent()) {
                            Categoria categoriaExistente = categoriaOptional.get();
                            LOGGER.info("Alterando Categoria... id:{}", categoria.getId());

                            LOGGER.debug("Payload: {}",categoria);
                            LOGGER.debug("Categoria Existente: {}", categoria);

                            categoriaExistente.setCodigoCategoria(codigocategoria);
                            categoriaExistente.setNomeCategoria(nomeCategoria);
                            categoriaExistente.setFornecedor(conexaoFornecedor.findByFornecedorId(id));
                            conexaoCategoria.save(categoriaExistente);
                        }
                    }
                    if (!(iLinhaCategoriaRepository.existsByCodLinhaCategoria(codLinhaCategoria))) {
                        linhaCategoria.setCodLinhaCategoria(codLinhaCategoria);
                        linhaCategoria.setNomeLinha(nomeLinha);
                        linhaCategoria.setCategoria(conexaoCategoria.findByCodigoCategoria1(codigocategoria));
                        iLinhaCategoriaRepository.save(linhaCategoria);

                    } else if (iLinhaCategoriaRepository.existsByCodLinhaCategoria(codLinhaCategoria)) {
                        linhaCategoria = linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(codLinhaCategoria);
                        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodLinhaCategoria(codLinhaCategoria);
                        LOGGER.info("Alterando linha... id:{}", linhaCategoria.getId());
                        LOGGER.debug("Payload: {}", linhaCategoria);
                        LOGGER.debug("Linha Categoria Existente: {}", linhaCategoria);

                        if (linhaCategoriaOptional.isPresent()) {
                            LinhaCategoria linhaExistente = linhaCategoriaOptional.get();
                            linhaExistente.setCodLinhaCategoria(codLinhaCategoria);
                            linhaExistente.setNomeLinha(nomeLinha);
                            linhaCategoria.setCategoria(conexaoCategoria.findByCodigoCategoria1(codigocategoria));
                            iLinhaCategoriaRepository.save(linhaExistente);
                        }
                    }
                    if (iProdutoRepository.existsByCodProduto(codProduto)) {
                        produto = produtoService.findByCodProduto(codProduto);
                        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodProduto(codProduto);
                        LOGGER.info("Atualizando produto... id:[{}]", produto.getId());
                        LOGGER.debug("Payload: {}", produto);
                        LOGGER.debug("produto Existente:{}", produto);

                        if (produtoOptional.isPresent()) {
                            Produto produtoExistente = produtoOptional.get();

                            produtoExistente.setCodProduto(codProduto);
                            produtoExistente.setNome(nomeProduto);
                            produtoExistente.setPreco(preco);
                            produtoExistente.setUniCaixa(uniCaixa);
                            produtoExistente.setPesoUni(peso);
                            produtoExistente.setUnidadeMedida(unimedida);
                            produtoExistente.setValidade(datavalidade);
                            produtoExistente.setLinhaCategoria(linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(codLinhaCategoria));
                            iProdutoRepository.save(produtoExistente);
                        }
                    } else if (!iProdutoRepository.existsByCodProduto(codProduto)) {

                        produto.setCodProduto(codProduto);
                        produto.setNome(nomeProduto);
                        produto.setPreco(preco);
                        produto.setUniCaixa(uniCaixa);
                        produto.setPesoUni(peso);
                        produto.setUnidadeMedida(unimedida);
                        produto.setValidade(datavalidade);
                        produto.setLinhaCategoria(linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(codLinhaCategoria));

                        iProdutoRepository.save(produto);
                    } else {
                        LOGGER.info("Produto {} Pretence a outro Fornecedor", produto.getId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
