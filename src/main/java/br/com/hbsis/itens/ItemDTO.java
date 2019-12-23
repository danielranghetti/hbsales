package br.com.hbsis.itens;

public class ItemDTO {
    private Long id;
    private Long produto;
    private Long pedido;
    private int quantidade;



    public ItemDTO() {
    }

    public ItemDTO(Long id, Long produto, Long pedido, int quantidade) {
        this.id = id;
        this.produto = produto;
        this.pedido = pedido;
        this.quantidade = quantidade;
    }
    public static  ItemDTO of(Item item){
        return new ItemDTO(
          item.getId(),
          item.getPedido().getId(),
                item.getProduto().getId(),
                item.getQuantidade()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
        this.produto = produto;
    }

    public Long getPedido() {
        return pedido;
    }

    public void setPedido(Long pedido) {
        this.pedido = pedido;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", produto=" + produto +
                ", pedido=" + pedido +
                ", quantidade=" + quantidade +
                '}';
    }
}
