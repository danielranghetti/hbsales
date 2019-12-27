package br.com.hbsis.conexao;



import br.com.hbsis.itens.Item;


import java.util.List;

public class InvoiceDTO {

    private String  cnpjFornecedor;
    private String employeeUuid;
    private List<InvoiceItemDTO> invoiceItemDTOSet;
    private double totalValue;

    public InvoiceDTO() {
    }

    public InvoiceDTO(String cnpjFornecedor, String employeeUuid, List<InvoiceItemDTO> invoiceItemDTOSet, double totalValue) {
        this.cnpjFornecedor = cnpjFornecedor;
        this.employeeUuid = employeeUuid;
        this.invoiceItemDTOSet = invoiceItemDTOSet;
        this.totalValue = totalValue;
    }
    public static  InvoiceDTO parser(String cnpjFornecedor, String uuid, List<Item> items, double totalValue){
        return new InvoiceDTO(cnpjFornecedor,uuid, InvoiceItemDTO.parserToList(items), totalValue );
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public List<InvoiceItemDTO> getInvoiceItemDTOSet() {
        return invoiceItemDTOSet;
    }

    public void setInvoiceItemDTOSet(List<InvoiceItemDTO> invoiceItemDTOSet) {
        this.invoiceItemDTOSet = invoiceItemDTOSet;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "cnpjFornecedor='" + cnpjFornecedor + '\'' +
                ", employeeUuid='" + employeeUuid + '\'' +
                ", invoiceItemDTOSet=" + invoiceItemDTOSet +
                ", totalValue=" + totalValue +
                '}';
    }
}
