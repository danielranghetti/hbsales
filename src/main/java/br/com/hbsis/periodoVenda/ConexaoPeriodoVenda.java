package br.com.hbsis.periodoVenda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ConexaoPeriodoVenda {

    private final IPeriodoVendaRepository iPeriodoVendaRepository;

    @Autowired
    public ConexaoPeriodoVenda(IPeriodoVendaRepository iPeriodoVendaRepository) {
        this.iPeriodoVendaRepository = iPeriodoVendaRepository;
    }

    public PeriodoVenda save(PeriodoVenda periodoVenda){
        try {
            periodoVenda = iPeriodoVendaRepository.save(periodoVenda);
        }catch (Exception e){
            e.printStackTrace();
        }
        return periodoVenda;
    }

    public void deletePorId(Long id){
        this.iPeriodoVendaRepository.deleteById(id);
    }

    public PeriodoVenda findByPeriodoVendaId(Long id) {
        Optional<PeriodoVenda> periodoVendaOptional = this.iPeriodoVendaRepository.findById(id);

        if (periodoVendaOptional.isPresent()) {
            return periodoVendaOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Optional<PeriodoVenda> findById(Long id) {
        Optional<PeriodoVenda> periodoVendaOptional = this.iPeriodoVendaRepository.findById(id);

        if (periodoVendaOptional.isPresent()) {
            return periodoVendaOptional;
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public  boolean dataExistente(LocalDate dataInicio, Long id){
        return iPeriodoVendaRepository.existDateAberta(dataInicio, id) >=1;

    }


}
