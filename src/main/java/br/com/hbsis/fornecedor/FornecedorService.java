package br.com.hbsis.fornecedor;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaDTO;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.categoria.ConexaoCategoria;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

@Service
 class FornecedorService {


    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);

    private final ConexaoFornecedor conexaoFornecedor;
    private final CategoriaService categoriaService;
    private final ConexaoCategoria conexaoCategoria;

    @Autowired
    public FornecedorService(ConexaoFornecedor conexaoFornecedor, @Lazy CategoriaService categoriaService, ConexaoCategoria conexaoCategoria) {
        this.conexaoFornecedor = conexaoFornecedor;
        this.categoriaService = categoriaService;
        this.conexaoCategoria = conexaoCategoria;

    }

    public FornecedorDTO findById(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.conexaoFornecedor.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public FornecedorDTO save(FornecedorDTO fornecedorDTO) {

        this.validate(fornecedorDTO);


        LOGGER.info("Salvando fornecedor");
        LOGGER.debug("Fornecedor: {}", fornecedorDTO);

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelefone(fornecedorDTO.getTelefone());
        fornecedor.seteMail(fornecedorDTO.geteMail());

        fornecedor = this.conexaoFornecedor.save(fornecedor);
        return FornecedorDTO.of(fornecedor);
    }

    private void validate(FornecedorDTO fornecedorDTO) {
        LOGGER.info("Validando Fornecedor");
        if (fornecedorDTO == null) {
            throw new IllegalArgumentException("FornecedorDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(fornecedorDTO.getRazaoSocial())) {
            throw new IllegalArgumentException("Razão Social não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getCnpj())) {
            throw new IllegalArgumentException("CNPJ não deve ser nulo/vazio");
        }
        if (!(StringUtils.isNumeric(fornecedorDTO.getCnpj()))){
            throw  new IllegalArgumentException("Cnpj não deve conter letras somente números");
        }

        if (fornecedorDTO.getCnpj().length() != 14) {
            throw new IllegalArgumentException("CNPJ deve conter 14 números");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getNomeFantasia())) {
            throw new IllegalArgumentException("Nome fantasia não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getEndereco())) {
            throw new IllegalArgumentException("Endereço não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedorDTO.getTelefone())) {
            throw new IllegalArgumentException("Telefone não deve ser nulo/vazio");
        }
        if (!(StringUtils.isNumeric(fornecedorDTO.getTelefone()))){
            throw new IllegalArgumentException("Telefone não deve conter letras apenas números");
        }
        if (fornecedorDTO.getTelefone().length() > 14 || fornecedorDTO.getTelefone().length() < 13) {
            throw new IllegalArgumentException("Telefone deve conter entre 13 e 14 números");
        }
        if (StringUtils.isEmpty(fornecedorDTO.geteMail())) {
            throw new IllegalArgumentException("E-mail não deve ser nulo/vazio");
        }
        if (!isCNPJ(fornecedorDTO.getCnpj())){
            throw new IllegalArgumentException("Cnpj invalido");
        }
    }

    public FornecedorDTO update(FornecedorDTO fornecedorDTO, Long id) {
        Optional<Fornecedor> fornecedorExistenteOptional = this.conexaoFornecedor.findById(id);
        this.validate(fornecedorDTO);

        if (fornecedorExistenteOptional.isPresent()) {
            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();
            Fornecedor fornecedor;
            fornecedor = conexaoFornecedor.findByFornecedorId(id);
            List<Categoria> categorias= conexaoCategoria.findByFornecedor(fornecedor);

            LOGGER.info("Atualizando fornecedor... id: [{}]", fornecedorExistente.getId());
            LOGGER.debug("Payload: {}", fornecedorDTO);
            LOGGER.debug("Fornecedor Existente: {}", fornecedorExistente);

            fornecedorExistente.setRazaoSocial(fornecedorDTO.getRazaoSocial());
            fornecedorExistente.setCnpj(fornecedorDTO.getCnpj());
            fornecedorExistente.setNomeFantasia(fornecedorDTO.getNomeFantasia());
            fornecedorExistente.setEndereco(fornecedorDTO.getEndereco());
            fornecedorExistente.setTelefone(fornecedorDTO.getTelefone());
            fornecedorExistente.seteMail(fornecedorDTO.geteMail());

            fornecedorExistente = this.conexaoFornecedor.save(fornecedorExistente);

            for (Categoria categoria : categorias) {
                categoria.setCodigoCategoria(categoria.getCodigoCategoria().substring(7, 10));
                categoria.setFornecedor(fornecedorExistente);
                categoriaService.update(CategoriaDTO.of(categoria), categoria.getId());
            }
            return FornecedorDTO.of(fornecedorExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para fornecedor de ID: [{}]", id);

        this.conexaoFornecedor.deletePorId(id);
    }
    public static boolean isCNPJ(String CNPJ) {
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return(false);

        char dig13, dig14;
        int sm, i, r, num, peso;


        try {

            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
                num = (int)(CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char)((11-r) + 48);

            sm = 0;
            peso = 2;
            for (i=12; i>=0; i--) {
                num = (int)(CNPJ.charAt(i)- 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char)((11-r) + 48);


            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }




}

