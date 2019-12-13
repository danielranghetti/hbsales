package br.com.hbsis.produto;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.categoria.ICategoriaRepository;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import br.com.hbsis.linhaCategoria.ILinhaCategoriaRepository;
import br.com.hbsis.linhaCategoria.LinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final IProdutoRepository iProdutoRepository;
    private final IFornecedorRepository iFornecedorRepository;
    private final ICategoriaRepository iCategoriaRepository;
    private final LinhaCategoriaService linhaCategoriaService;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;




    public ProdutoService(ILinhaCategoriaRepository iLinhaCategoriaRepository, IProdutoRepository iProdutoRepository, IFornecedorRepository iFornecedorRepository, ICategoriaRepository iCategoriaRepository, LinhaCategoriaService linhaCategoriaService, CategoriaService categoriaService, FornecedorService fornecedorService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.iProdutoRepository = iProdutoRepository;
        this.iFornecedorRepository = iFornecedorRepository;
        this.iCategoriaRepository = iCategoriaRepository;
        this.linhaCategoriaService = linhaCategoriaService;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;

    }

    public List<Produto> saveAll(List<Produto> produto) throws Exception {

        return iProdutoRepository.saveAll(produto);
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
        produto.setLinhaCategoria(linhaCategoriaService.findByLinhaCategoriaId(produtoDTO.getLinhaCategoria()));

        produto = this.iProdutoRepository.save(produto);
        return ProdutoDTO.of(produto);
    }

    public String validarCodigo(String codigo) {
        String codigoProcessador = StringUtils.leftPad(codigo, 10, "0");
        return codigoProcessador;
    }

    public ProdutoDTO finById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (((Optional) produtoOptional).isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }
        throw new IllegalArgumentException(String.format("esse %s não existe", id));

    }

    public Produto findByProdutoId(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public Produto findByCodProduto( String codProduto) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodProduto(codProduto);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }

        throw new IllegalArgumentException(String.format("Codigo produto %s não existe",codProduto));
    }


    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {
        Optional<Produto> produtoExistenteOptional = this.iProdutoRepository.findById(id);

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

        String arquivo = " csv_produtos.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String titulo[] = {"código", "nome", "preço", "quantidade de unidades por caixa", "peso ", "validade", " código da linha de categoria", "nome da Linha de Categoria",
                "código da Categoria", " nome da Categoria", "CNPJ", "razão social", ""};
        icsvWriter.writeNext(titulo);

        for (Produto linha : iProdutoRepository.findAll()) {
            icsvWriter.writeNext(new String[]{linha.getCodProduto(), linha.getNome(), "R$" + (linha.getPreco()), String.valueOf(linha.getUniCaixa()), linha.getPesoUni() + linha.getUnidadeMedida(),
                    String.valueOf(linha.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyy"))), linha.getLinhaCategoria().getCodLinhaCategoria(), linha.getLinhaCategoria().getNomeLinha(), linha.getLinhaCategoria().getCategoria().getCodigoCategoria(),
                    linha.getLinhaCategoria().getCategoria().getNomeCategoria(), mascaraCnpj(linha.getLinhaCategoria().getCategoria().getFornecedor().getCnpj()), linha.getLinhaCategoria().getCategoria().getFornecedor().getRazaoSocial()
            });
        }

    }

    public void findAllFornecedor(HttpServletResponse response) throws Exception {

        String arquivo = " csv_produtos-fornecedor.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String titulo[] = {"código", "nome", "preço", "quantidade de unidades por caixa", "peso ", "validade", " código da linha de categoria", "nome da Linha de Categoria",
                "código da Categoria", "nome categoria"};
        icsvWriter.writeNext(titulo);

        for (Produto linha : iProdutoRepository.findAll()) {
            icsvWriter.writeNext(new String[]{linha.getCodProduto(), linha.getNome(), "R$" + (linha.getPreco()), String.valueOf(linha.getUniCaixa()), linha.getPesoUni() + linha.getUnidadeMedida(),
                    String.valueOf(linha.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyy"))), linha.getLinhaCategoria().getCodLinhaCategoria(), linha.getLinhaCategoria().getNomeLinha(), linha.getLinhaCategoria().getCategoria().getCodigoCategoria(),
                    linha.getLinhaCategoria().getCategoria().getNomeCategoria()
            });
        }

    }


    public String mascaraCnpj(String CNPJ) {
        String mascara = CNPJ.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        return mascara;
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
                LinhaCategoria linhaCategoria = new LinhaCategoria();

                if (iProdutoRepository.existsByCodProduto(dados[0])) {
                    LOGGER.info("Produto: {}", dados[0] + " já existe");
                } else if (iLinhaCategoriaRepository.existsByCodLinhaCategoria(dados[6])) {

                    String data = dados[5].toString();
                    int dia = Integer.parseInt(data.substring(0, 2));
                    int mes = Integer.parseInt(data.substring(3, 5));
                    int ano = Integer.parseInt(data.substring(6, 10));
                    LocalDate datavalidade = LocalDate.of(ano, mes, dia);

                    produto.setCodProduto((dados[0]));
                    produto.setNome(dados[1]);
                    produto.setPreco(Double.parseDouble(dados[2].trim().replace(',', '.').replaceAll("[A-Z$]", "")));
                    produto.setUniCaixa(Integer.parseInt(dados[3]));
                    produto.setPesoUni(Double.parseDouble(dados[4].replaceAll("[A-Za-z]", "")));
                    produto.setUnidadeMedida(dados[4].replaceAll("[0-9.]", ""));
                    produto.setValidade(datavalidade);
                    linhaCategoria = linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(dados[6]);

                    produto.setLinhaCategoria(linhaCategoria);
                    resultadoLeitura.add(produto);

                } else if (!iProdutoRepository.existsByCodProduto(dados[6])) {

                    String data = dados[5].toString();
                    int dia = Integer.parseInt(data.substring(0, 2));
                    int mes = Integer.parseInt(data.substring(3, 5));
                    int ano = Integer.parseInt(data.substring(6, 10));
                    LocalDate datavalidade = LocalDate.of(ano, mes, dia);

                    produto.setCodProduto((dados[0]));
                    produto.setNome(dados[1]);
                    produto.setPreco(Double.parseDouble(dados[2].trim().replace(',', '.').replaceAll("[A-Z$]", "")));
                    produto.setUniCaixa(Integer.parseInt(dados[3]));
                    produto.setPesoUni(Double.parseDouble(dados[4].replaceAll("[A-Za-z]", "")));
                    produto.setUnidadeMedida(dados[4].replaceAll("[0-9]", ""));
                    produto.setValidade(datavalidade);
                    linhaCategoria = linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(dados[6]);
                    produto.setLinhaCategoria(linhaCategoria);
                    resultadoLeitura.add(produto);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iProdutoRepository.saveAll(resultadoLeitura);

    }

    public void importaProdutoFornecedor(Long id, MultipartFile file) throws Exception {
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



                if (iFornecedorRepository.existsById(id)) {
                    if (!(iCategoriaRepository.existsByCodigoCategoria(resultado[8]))) {
                        categoria.setNomeCategoria(resultado[9]);
                        categoria.setCodigoCategoria(resultado[8]);
                        categoria.setFornecedor(fornecedorService.findByFornecedorId(id));
                        iCategoriaRepository.save(categoria);
                    } else if (iCategoriaRepository.existsByCodigoCategoria(resultado[8])) {
                        categoria = categoriaService.findByCodigoCategoria(resultado[8]);
                        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findByCodigoCategoria(resultado[8]);

                        if (categoriaOptional.isPresent()) {
                            Categoria categoriaExistente = categoriaOptional.get();
                            LOGGER.info("Alterando Categoria... id:{}", categoria.getId());

                            LOGGER.debug("Payload: {}", categoria);
                            LOGGER.debug("Categoria Existente: {}", categoria);

                            categoriaExistente.setCodigoCategoria(resultado[8]);
                            categoriaExistente.setNomeCategoria(resultado[9]);
                            categoriaExistente.setFornecedor(fornecedorService.findByFornecedorId(id));
                            iCategoriaRepository.save(categoria);
                        }
                    }
                    if (!(iLinhaCategoriaRepository.existsByCodLinhaCategoria(resultado[6]))) {
                        linhaCategoria.setCodLinhaCategoria(resultado[6]);
                        linhaCategoria.setNomeLinha(resultado[7]);
                        categoria = categoriaService.findByCodigoCategoria(resultado[8]);
                        iLinhaCategoriaRepository.save(linhaCategoria);

                    } else if (iLinhaCategoriaRepository.existsByCodLinhaCategoria(resultado[6])) {

                        linhaCategoria = linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(resultado[6]);
                        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findByCodLinhaCategoria(resultado[6]);
                        LOGGER.info("Alterando linha... id:{}", linhaCategoria.getId());
                        LOGGER.debug("Payload: {}", linhaCategoria);
                        LOGGER.debug("Linha Categoria Existente: {}", linhaCategoria);
                        if (linhaCategoriaOptional.isPresent()) {
                            LinhaCategoria linhaExistente = linhaCategoriaOptional.get();
                            linhaExistente.setCodLinhaCategoria(resultado[6]);
                            linhaExistente.setNomeLinha(resultado[7]);
                            categoria = categoriaService.findByCodigoCategoria(resultado[8]);
                            iLinhaCategoriaRepository.save(linhaCategoria);
                        }
                    }
                    if (iProdutoRepository.existsByCodProduto(resultado[0])) {
                        produto = findByCodProduto(resultado[0]);
                        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodProduto(resultado[0]);
                        LOGGER.info("Atualizando produto... id:[{}]", produto.getId());
                        LOGGER.debug("Payload: {}");
                        LOGGER.debug("produto Existente:{}", produto);

                        if (produtoOptional.isPresent()) {
                            Produto produtoExistente = produtoOptional.get();
                            String data = resultado[5].toString();
                            int dia = Integer.parseInt(data.substring(0, 2));
                            int mes = Integer.parseInt(data.substring(3, 5));
                            int ano = Integer.parseInt(data.substring(6, 10));
                            LocalDate datavalidade = LocalDate.of(ano, mes, dia);

                            produtoExistente.setCodProduto((resultado[0]));
                            produtoExistente.setNome(resultado[1]);
                            produtoExistente.setPreco(Double.parseDouble(resultado[2].trim().replace(',', '.').replaceAll("[A-Z$]", "")));
                            produtoExistente.setUniCaixa(Integer.parseInt(resultado[3]));
                            produtoExistente.setPesoUni(Double.parseDouble(resultado[4].replaceAll("[A-Za-z]", "")));
                            produtoExistente.setUnidadeMedida(resultado[4].replaceAll("[0-9.]", ""));
                            produtoExistente.setValidade(datavalidade);
                            linhaCategoria = linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(resultado[6]);

                            System.out.println(produto);
                        }
                    } else if (!iProdutoRepository.existsByCodProduto(resultado[0])) {
                        String data = resultado[5].toString();
                        int dia = Integer.parseInt(data.substring(0, 2));
                        int mes = Integer.parseInt(data.substring(3, 5));
                        int ano = Integer.parseInt(data.substring(6, 10));
                        LocalDate datavalidade = LocalDate.of(ano, mes, dia);

                        produto.setCodProduto((resultado[0]));
                        produto.setNome(resultado[1]);
                        produto.setPreco(Double.parseDouble(resultado[2].trim().replace(',', '.').replaceAll("[A-Z$]", "")));
                        produto.setUniCaixa(Integer.parseInt(resultado[3]));
                        produto.setPesoUni(Double.parseDouble(resultado[4].replaceAll("[A-Za-z]", "")));
                        produto.setUnidadeMedida(resultado[4].replaceAll("[0-9.]", ""));
                        produto.setValidade(datavalidade);
                        linhaCategoria = linhaCategoriaService.findByLinhaCategoriaCodLinhaCategoria(resultado[6]);
                        produto.setLinhaCategoria(linhaCategoria);


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