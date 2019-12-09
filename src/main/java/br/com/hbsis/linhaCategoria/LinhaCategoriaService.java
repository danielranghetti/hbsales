package br.com.hbsis.linhaCategoria;


import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaDTO;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.fornecedor.FornecedorService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.SetOverrideType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;


    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaService categoriaService,FornecedorService fornecedorService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {
        this.validate(linhaCategoriaDTO);
        LOGGER.info("Salvando linha da categoria");
        LOGGER.debug("LinhaCategoria {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();


        String codigoRecebido = linhaCategoriaDTO.getCodLinhaCategoria();

        String codigoCompelto = validarCodigo(codigoRecebido);
        codigoCompelto = codigoCompelto.toUpperCase();


        linhaCategoria.setCodLinhaCategoria(codigoCompelto);
        linhaCategoria.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
        linhaCategoria.setCategoria(categoriaService.findByCategoriaId(linhaCategoriaDTO.getCategoria()));


        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);
        return LinhaCategoriaDTO.of(linhaCategoria);
    }
    public String validarCodigo(String codigo){
        String codigoCorrigido = StringUtils.leftPad(codigo,10, "0");
        return codigoCorrigido;
    }

    public List<LinhaCategoria> saveAll(List<LinhaCategoria> linhaCategorias) throws Exception {
        return iLinhaCategoriaRepository.saveAll(linhaCategorias);
    }

    public LinhaCategoriaDTO findByid(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (((Optional) linhaCategoriaOptional).isPresent()) {
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public LinhaCategoria findByLinhaCategoriaId(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return linhaCategoriaOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {
        Optional<LinhaCategoria> linhaCategoriaExistenteOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaExistenteOptional.isPresent()) {
            LinhaCategoria linhaCategoriaExistente = linhaCategoriaExistenteOptional.get();

            LOGGER.info("Atualizando linha categoria... id:[{}]", linhaCategoriaExistente.getId());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha Categoria Existente:{}", linhaCategoriaExistente);

            String codigoRecebido = linhaCategoriaDTO.getCodLinhaCategoria();

            String codigoCompelto = validarCodigo(codigoRecebido);
            codigoCompelto = codigoCompelto.toUpperCase();


            linhaCategoriaExistente.setCodLinhaCategoria(codigoCompelto);
            linhaCategoriaExistente.setNomeLinha(linhaCategoriaDTO.getNomeLinha());
            linhaCategoriaExistente.setCategoria(categoriaService.findByCategoriaId(linhaCategoriaDTO.getCategoria()));

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return LinhaCategoriaDTO.of(linhaCategoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para linha de categoria de ID:[{}]", id);
        this.iLinhaCategoriaRepository.deleteById(id);
    }

    //Faz a exportação
    public void findAll(HttpServletResponse response) throws Exception {
        String nomeArquivo = "linhaCategorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String headerCSV[] = {"codigo", "nome", "codigo_categoria", "nome_categoria"};
        icsvWriter.writeNext(headerCSV);

        for (LinhaCategoria linha : iLinhaCategoriaRepository.findAll()) {
            icsvWriter.writeNext(new String[]{linha.getCodLinhaCategoria(), linha.getNomeLinha(), linha.getCategoria().getCodigoCategoria(), linha.getCategoria().getNomeCategoria()}
            );
        }


    }

        //faz a importação
    public List<LinhaCategoria> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1).build();


        List<LinhaCategoria> leitura = new ArrayList<>();
        List<String[]> linhas = csvReader.readAll();

        for (String[] lista : linhas) {
            try {

                String[] resultado = lista[0].replaceAll("\"", "").split(";");

                LinhaCategoria linhaCategoria = new LinhaCategoria();
                Categoria categoria = new Categoria();
                CategoriaDTO categoriaDTO = new CategoriaDTO();


                linhaCategoria.setId(Long.parseLong(resultado[0]));
                categoriaDTO = categoriaService.findById(Long.parseLong(resultado[1]));
                linhaCategoria.setCodLinhaCategoria((resultado[2]));
                linhaCategoria.setNomeLinha(resultado[3]);

                categoria.setId(categoriaDTO.getId());
                categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
                categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());

                //categoria.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getFornecedor()));


                linhaCategoria.setCategoria(categoria);

                leitura.add(linhaCategoria);
                System.out.println(leitura);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iLinhaCategoriaRepository.saveAll(leitura);
    }
    private void validate(LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Validando linha da categoria");
        if (linhaCategoriaDTO == null) {
            throw new IllegalArgumentException("LinhaCategoriaDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getCodLinhaCategoria())){
            throw new IllegalArgumentException("Codigo da linga categoria não deve ser nulo");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getNomeLinha())){
            throw  new IllegalArgumentException("Nome linha categoria não dve ser nulo");
        }
        if (StringUtils.isEmpty(Long.toString(linhaCategoriaDTO.getCategoria()))){
            throw new IllegalArgumentException("categoria da linha categoria não cadastrada");
        }
    }


}


