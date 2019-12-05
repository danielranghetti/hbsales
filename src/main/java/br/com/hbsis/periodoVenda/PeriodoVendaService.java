package br.com.hbsis.periodoVenda;


import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PeriodoVendaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendaService.class);

    private final IPeriodoVendaRepository iPeriodoVendaRepository;
    private final IFornecedorRepository iFornecedorRepository;
    private final FornecedorService fornecedorService;

    public PeriodoVendaService(IPeriodoVendaRepository iPeriodoVendaRepository, IFornecedorRepository iFornecedorRepository, FornecedorService fornecedorService) {
        this.iPeriodoVendaRepository = iPeriodoVendaRepository;
        this.iFornecedorRepository = iFornecedorRepository;
        this.fornecedorService = fornecedorService;
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
        periodoVenda.setFornecedor(fornecedorService.findByFornecedorId(periodoVendaDTO.getFornecedor()));

        periodoVenda = this.iPeriodoVendaRepository.save(periodoVenda);
        return PeriodoVendaDTO.of(periodoVenda);
    }

    public void validate(PeriodoVendaDTO periodoVendaDTO){
        LOGGER.info("Validando Periodo Venda");

        if(periodoVendaDTO == null){
            throw new IllegalArgumentException("Periodo Venda não pode ser nulo.");
        }
    }

    public PeriodoVendaDTO findById(Long id) {
        Optional<PeriodoVenda> periodoVendaOptional = this.iPeriodoVendaRepository.findById(id);

        if (periodoVendaOptional.isPresent()) {
            return PeriodoVendaDTO.of(periodoVendaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public PeriodoVendaDTO update(PeriodoVendaDTO periodoVendaDTO, Long id) {
        Optional<PeriodoVenda> periodoVendaExistenteOptional = this.iPeriodoVendaRepository.findById(id);

        if (periodoVendaExistenteOptional.isPresent()) {
            PeriodoVenda periodoVendaExistente = periodoVendaExistenteOptional.get();

            LOGGER.info("Atualizando periodo de vanda... id: [{}]", periodoVendaExistente.getId());
            LOGGER.debug("Payload: {}", periodoVendaDTO);
            LOGGER.debug("Usuario Existente: {}", periodoVendaExistente);

            periodoVendaExistente.setId(periodoVendaDTO.getId());
            periodoVendaExistente.setDataInicio(periodoVendaDTO.getDataInicio());
            periodoVendaExistente.setDataFinal(periodoVendaDTO.getDataFinal());
            periodoVendaExistente.setDataRetirada(periodoVendaDTO.getDataRetirada());
            periodoVendaExistente.setFornecedor(fornecedorService.findByFornecedorId(periodoVendaDTO.getFornecedor()));

            periodoVendaExistente = this.iPeriodoVendaRepository.save(periodoVendaExistente);
            return PeriodoVendaDTO.of(periodoVendaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }
    public  void  delete(Long id){
        LOGGER.info("Executando delete para o  periodo de venda: {}", id);

        this.iPeriodoVendaRepository.deleteById(id);
    }
}
