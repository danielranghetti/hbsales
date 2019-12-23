CREATE TABLE seg_itens(
    id             BIGINT IDENTITY (1, 1)  PRIMARY KEY NOT NULL,
    id_pedido      BIGINT REFERENCES seg_pedidos(id) NOT NULL,
    id_produto     INTEGER REFERENCES seg_produtos(id) NOT NULL,
    quantidade     INT           NOT NULL
);