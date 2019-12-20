package br.com.hbsis.ferramentas;

import org.springframework.stereotype.Service;

@Service
public class MascaraCnpj {
    public String formatCnpj(String CNPJ) {
        String mascara = CNPJ.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        return mascara;
    }
}
