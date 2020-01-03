package br.com.hbsis.periodoVenda;


import br.com.hbsis.fornecedor.ConexaoFornecedor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class PeriodoVendaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendaService.class);

    private final ConexaoPeriodoVenda conexaoPeriodoVenda;
   private final ConexaoFornecedor conexaoFornecedor;


    public PeriodoVendaService(ConexaoPeriodoVenda conexaoPeriodoVenda, ConexaoFornecedor conexaoFornecedor) {
        this.conexaoPeriodoVenda = conexaoPeriodoVenda;
        this.conexaoFornecedor = conexaoFornecedor;
    }

    public PeriodoVendaDTO save(PeriodoVendaDTO periodoVendaDTO) {

        this.validate(periodoVendaDTO);

        LOGGER.info("Salvando Periodo de venda");
        LOGGER.debug("PeriodoVenda: {}", periodoVendaDTO);

        PeriodoVenda periodoVenda = new PeriodoVenda();

        periodoVenda.setId(periodoVendaDTO.getId());
        periodoVenda.setDataInicio(periodoVendaDTO.getDataInicio());
        periodoVenda.setDataFinal(periodoVendaDTO.getDataFinal());
        periodoVenda.setDataRetirada(periodoVendaDTO.getDataRetirada());
        periodoVenda.setDescricao(periodoVendaDTO.getDescricao());
        periodoVenda.setFornecedor(conexaoFornecedor.findByFornecedorId(periodoVendaDTO.getFornecedor()));

        periodoVenda = this.conexaoPeriodoVenda.save(periodoVenda);
        return PeriodoVendaDTO.of(periodoVenda);
    }

    public void validate(PeriodoVendaDTO periodoVendaDTO) {
        LOGGER.info("Validando Periodo Venda");

        if (periodoVendaDTO == null) {
            throw new IllegalArgumentException("Periodo Venda não pode ser nulo.");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendaDTO.getDataInicio()))){
            throw  new  IllegalArgumentException("Data inicio não pode ser nula");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendaDTO.getDataFinal()))){
            throw new  IllegalArgumentException("Data final não pode ser nula");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendaDTO.getDataRetirada()))){
            throw  new IllegalArgumentException("Data de retirada não deve ser nula");
        }
        if (StringUtils.isEmpty(periodoVendaDTO.getDescricao())){
            throw  new  IllegalArgumentException("descrição não deve ser nula");
        }
        if (StringUtils.isEmpty(String.valueOf(periodoVendaDTO.getFornecedor()))){
            throw new  IllegalArgumentException("fornecedor não deve ser nulo");
        }
        if (periodoVendaDTO.getDataInicio().isBefore(LocalDate.now()) || periodoVendaDTO.getDataFinal().isBefore(LocalDate.now()) || periodoVendaDTO.getDataRetirada().isBefore(LocalDate.now())){
            throw new  IllegalArgumentException("As datas não podem ser inferior a de hoje");
        }
        if (conexaoPeriodoVenda.dataExistente(periodoVendaDTO.getDataInicio(), periodoVendaDTO.getFornecedor())){
            throw new  IllegalArgumentException("O fornecedor não pode ter dois periodos de venda ao mesmo tempo");
        }
        if (periodoVendaDTO.getDataFinal().isBefore(periodoVendaDTO.getDataInicio()))  {
            throw  new  IllegalArgumentException("Data final não pode ser anterior que a dara de inicio");
        }
        if (periodoVendaDTO.getDataRetirada().isBefore(periodoVendaDTO.getDataFinal())){
            throw new IllegalArgumentException(" A data  de retirada não pode ser anterior a de final");
        }


    }
    public PeriodoVendaDTO findById(Long id) {
        Optional<PeriodoVenda> periodoVendaOptional = this.conexaoPeriodoVenda.findById(id);

        if (periodoVendaOptional.isPresent()) {
            return PeriodoVendaDTO.of(periodoVendaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }



    public PeriodoVendaDTO update(PeriodoVendaDTO periodoVendaDTO, Long id) {
        Optional<PeriodoVenda> periodoVendaExistenteOptional = this.conexaoPeriodoVenda.findById(id);

        if (periodoVendaExistenteOptional.isPresent()) {
            PeriodoVenda periodoVendaExistente = periodoVendaExistenteOptional.get();

            LOGGER.info("Atualizando periodo de vanda... id: [{}]", periodoVendaExistente.getId());
            LOGGER.debug("Payload: {}", periodoVendaDTO);
            LOGGER.debug("Usuario Existente: {}", periodoVendaExistente);


            periodoVendaExistente.setDataInicio(periodoVendaDTO.getDataInicio());
            periodoVendaExistente.setDataFinal(periodoVendaDTO.getDataFinal());
            periodoVendaExistente.setDataRetirada(periodoVendaDTO.getDataRetirada());
            periodoVendaExistente.setFornecedor(conexaoFornecedor.findByFornecedorId(periodoVendaDTO.getFornecedor()));

            periodoVendaExistente = this.conexaoPeriodoVenda.save(periodoVendaExistente);
            return PeriodoVendaDTO.of(periodoVendaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para o  periodo de venda: {}", id);

        this.conexaoPeriodoVenda.deletePorId(id);
    }


}
