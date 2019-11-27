package br.com.hbsis.categoria;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import com.opencsv.CSVReader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);

    private final ICategoriaRepository iCategoriaRepository;
    private final IFornecedorRepository iFornecedorRepository;
    private final FornecedorService fornecedorService;

    public CategoriaService(ICategoriaRepository iCategoriaRepository, IFornecedorRepository iFornecedorRepository, FornecedorService fornecedorService) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.iFornecedorRepository = iFornecedorRepository;
        this.fornecedorService = fornecedorService;
    }

    public List<Categoria> findAll() {
        return iCategoriaRepository.findAll();
    }

    public List<String> listToPrint(){
        List<String> lista = new ArrayList<>();
        for(Categoria linhaCSV : iCategoriaRepository.findAll()){
            String construtor = linhaCSV.getId() + ";" + linhaCSV.getCodigoCategoria() + ";" + linhaCSV.getNomeCategoria() + ";" + linhaCSV.getFornecedor().getId()+ ";";
            lista.add(construtor);
        }
        return lista;
    }

    public List<Categoria> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<Categoria> resultadoLeitura = new ArrayList<>();
        String[] linha;
        while ((linha = csvReader.readNext()) != null) {
            Categoria categoria = new Categoria();

            FornecedorService fornecedorService = new FornecedorService(iFornecedorRepository);
            FornecedorDTO fornecedorDTO;
            Fornecedor fornecedor = new Fornecedor();

            fornecedorDTO = fornecedorService.findById(Long.parseLong(linha[3]));

            fornecedor.setId(fornecedorDTO.getId());
            fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
            fornecedor.setCnpj(fornecedorDTO.getCnpj());
            fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
            fornecedor.setEndereco(fornecedorDTO.getEndereco());
            fornecedor.setTelefone(fornecedorDTO.getTelefone());
            fornecedor.seteMail(fornecedorDTO.geteMail());

            categoria.setCodigoCategoria(Long.parseLong(linha[1]));
            categoria.setNomeCategoria(linha[2]);
            categoria.setFornecedor(fornecedor);

            resultadoLeitura.add(categoria);
        }
        reader.close();
        csvReader.close();
        return resultadoLeitura;
    }

    public List<Categoria> saveAll(List<Categoria> categoria) throws Exception {

        return iCategoriaRepository.saveAll(categoria);
    }


    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        this.validate(categoriaDTO);

        LOGGER.info("Salvando categoria");
        LOGGER.debug("Categoria: {}", categoriaDTO);

        Categoria categoria = new Categoria();
        categoria.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
        categoria.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getFornecedor()));
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());

        categoria = this.iCategoriaRepository.save(categoria);
        return CategoriaDTO.of(categoria);
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
        if (StringUtils.isEmpty(Long.toString(categoriaDTO.getCodigoCategoria()))) {
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

    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> categoriaExistenteOptional = this.iCategoriaRepository.findById(id);

        if (categoriaExistenteOptional.isPresent()) {
            Categoria categoriaExistente = categoriaExistenteOptional.get();

            LOGGER.info("Atualizando categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaExistente);

            categoriaExistente.setCodigoCategoria(categoriaDTO.getCodigoCategoria());
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
