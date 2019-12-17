create table seg_pedido
(
    id    BIGINT IDENTITY (1, 1) NOT NULL,
    codPedido VARCHAR(100)           NOT NULL,
    status VARCHAR(10)           NOT NULL,
    qtdCompra  INT            NOT NULL,
    id_produto INTEGER REFERENCES seg_produtos(id) NOT NULL,
    id_funcionario INTEGER REFERENCES seg_fornecedores(id) NOT NULL,
    id_periodo_venda INTEGER REFERENCES seg_periodo_venda(id) NOT NULL
);

create unique index ix_seg_pedido_01 on seg_pedido (codPedido asc);

