create table seg_pedidos
(
    id                  BIGINT IDENTITY (1, 1) NOT NULL,
    codigo_pedido       VARCHAR(10)           NOT NULL,
    id_funcionario      INTEGER REFERENCES seg_fornecedores(id) NOT NULL,
    id_produto          INTEGER REFERENCES seg_produtos(id) NOT NULL,
    qtd_compra          INTEGER       NOT NULL,
    id_periodo_venda    INTEGER REFERENCES seg_periodo_venda(id) NOT NULL,
    status              VARCHAR(10)      NOT NULL,
    data                DATE  NOT NULL
);

create unique index ix_seg_pedidos_01 on seg_pedidos (codigo_pedido asc);
