package br.com.hbsis.produto;


import br.com.hbsis.csv.CsvProduto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/produtos")
public class ProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);
    private final ProdutoService produtoService;
    private final CsvProduto csvProduto;

    @Autowired
    public ProdutoRest(ProdutoService produtoService, CsvProduto csvProduto) {
        this.produtoService = produtoService;
        this.csvProduto = csvProduto;

    }

    @PostMapping
    public ProdutoDTO save(@RequestBody ProdutoDTO produtoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de produtos...");
        LOGGER.debug("Payaload: {}", produtoDTO);

        return this.produtoService.save(produtoDTO);
    }

    @GetMapping("/{id}")
    private ProdutoDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id:[{}]", id);

        return this.produtoService.finById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO) {
        LOGGER.info("recebendo Update para produto de ID: {}", id);
        LOGGER.debug("payload: {}", produtoDTO);

        return this.produtoService.update(produtoDTO, id);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo delete para Produto de ID: {} ", id);

        this.produtoService.delete(id);
    }

    @GetMapping("/exporta-csv-produtos")
    public void findAll(HttpServletResponse response) throws Exception {
        csvProduto.csvToProdutoExport(response);
    }

    @GetMapping("/exporta-csvproduto-fornecedor")
    public void findAllFornecedor(HttpServletResponse response) throws Exception {
        csvProduto.csvToProdutoFornecedorExport(response);
    }

    @PostMapping("/importa-csv-produtos")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        csvProduto.importFromCsvProduto(file);
    }

    @PostMapping("/importa-csv-produtos-fornecedor/{id}")
    public void importCSVFornecedor(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws Exception {
        csvProduto.csvToProdutoFornecedor(id, file);
    }

}
