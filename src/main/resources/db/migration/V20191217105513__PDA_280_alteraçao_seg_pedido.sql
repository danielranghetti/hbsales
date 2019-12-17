DROP INDEX seg_pedido.ix_seg_pedido_01;
EXEC sp_rename 'seg_pedido.codPedido', 'cod_pedido', 'COLUMN';
EXEC sp_rename 'seg_pedido.qtdCompra', 'qtd_compra', 'COLUMN';
ALTER TABLE seg_pedido ALTER COLUMN cod_pedido VARCHAR(10) NOT NULL;
create unique index ix_seg_pedido_01 on seg_pedido (cod_pedido asc);