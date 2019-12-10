package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);

    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;
    private final IFornecedorRepository iFornecedorRepository;

    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService, IFornecedorRepository iFornecedorRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
        this.iFornecedorRepository = iFornecedorRepository;
    }



    //faz a exportaçao
    public void findAll(HttpServletResponse response) throws Exception {

        String nomeArquivo = "categorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter csvWriter = new CSVWriterBuilder(Writer)
                .withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build();

        String headerCSV[] = {"codigo_categoria", ";nome_categoria", "razao_social_fornecedor", ";cnpj_fornecedor "};
        csvWriter.writeNext(headerCSV);

        for (Categoria linha : iCategoriaRepository.findAll()) {
            csvWriter.writeNext(new String[]{linha.getCodigoCategoria(),linha.getNomeCategoria(),linha.getFornecedor().getRazaoSocial(),mascaraCnpj(linha.getFornecedor().getCnpj())}
            );
        }

    }
    public String mascaraCnpj(String CNPJ){
    String mascara = CNPJ.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    return mascara;
    }



    //faz a importação
    public List<Categoria> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1).build();


        List<Categoria> leitura = new ArrayList<>();
        List<String[]> linhas = csvReader.readAll();

        for (String[] lista : linhas) {
            try {

                String[] resultado = lista[0].replaceAll("\"", "").split(";");

                Categoria categoria = new Categoria();
                Fornecedor fornecedor = new Fornecedor();
                FornecedorDTO fornecedorDTO = new FornecedorDTO();

                if (iCategoriaRepository.existsByCodigoCategoria(resultado[0])){
                    LOGGER.info("Categoria: {}", resultado[0] + " já existe");
                }
                else if (iFornecedorRepository.existsByCnpj(resultado[3].replaceAll("[^0-9]",""))) {

                    categoria.setCodigoCategoria((resultado[0]));
                    categoria.setNomeCategoria((resultado[1]));
                    fornecedor = fornecedorService.findByFornecedorCnpj(resultado[3].replaceAll("[^0-9]", ""));

                    categoria.setFornecedor(fornecedor);
                    leitura.add(categoria);
                }
                else if (!iCategoriaRepository.existsByCodigoCategoria((resultado[0]))){
                    categoria.setCodigoCategoria((resultado[0]));
                    categoria.setNomeCategoria((resultado[1]));
                    fornecedor = fornecedorService.findByFornecedorCnpj(resultado[3].replaceAll("[^0-9]", ""));

                    categoria.setFornecedor(fornecedor);
                    leitura.add(categoria);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iCategoriaRepository.saveAll(leitura);
    }


    public List<Categoria> saveAll(List<Categoria> categoria) throws Exception {

        return iCategoriaRepository.saveAll(categoria);
    }


    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        this.validate(categoriaDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaDTO);

        Categoria categoria = new Categoria();

        String incial = "CAT";

        categoria.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getFornecedor()));
        String cnpj = categoria.getFornecedor().getCnpj();
        String ultDig = cnpj.substring(cnpj.length() - 4);

        String codigo = categoriaDTO.getCodigoCategoria();
        String codigoComZero = codigoValidar(codigo);

        categoria.setCodigoCategoria(incial + ultDig + codigoComZero);
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());

        categoria = this.iCategoriaRepository.save(categoria);
        return CategoriaDTO.of(categoria);
    }


    public String codigoValidar (String codigo){
        String codigoProcessador = StringUtils.leftPad(codigo, 3, "0");
        return codigoProcessador;
    }

    private void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("Validando categoria");
        if (categoriaDTO == null) {
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(Long.toString(categoriaDTO.getFornecedor()))) {
            throw new IllegalArgumentException("Fornecedo categoria não cadastrado");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome categoria não deve ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getCodigoCategoria())) {
            throw new IllegalArgumentException("Codigo categoria não deve ser nulo");
        }
    }

    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (((Optional) categoriaOptional).isPresent()) {
            return CategoriaDTO.of(categoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Categoria findByCodigoCategoria(String codigoCategoria) {
        Optional<Categoria> optionalCategoria = this.iCategoriaRepository.findByCodigoCategoria(codigoCategoria);

        if (optionalCategoria.isPresent()) {
            return optionalCategoria.get();
        }
        throw  new IllegalArgumentException(String.format("ID %s não existe", codigoCategoria));
    }

    public Categoria findByCategoriaId(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (((Optional) categoriaOptional).isPresent()) {
            return categoriaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptional = this.iCategoriaRepository.findById(id);

        if (categoriaExistenteOptional.isPresent()) {
            Categoria categoriaExistente = categoriaExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaExistente);

            String incial = "CAT";

            categoriaExistente.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getFornecedor()));
            String cnpj = categoriaExistente.getFornecedor().getCnpj();
            String ultDig = cnpj.substring(cnpj.length() - 4);

            String codigo = categoriaDTO.getCodigoCategoria();
            String codigoComZero = codigoValidar(codigo);
            codigoComZero = codigoComZero.toUpperCase();

            categoriaExistente.setCodigoCategoria(incial + ultDig + codigoComZero);
            categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());

            categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

            return CategoriaDTO.of(categoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria de ID: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }
}
