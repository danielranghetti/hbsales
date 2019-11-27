create table seg_categorias
(
    id    BIGINT IDENTITY (1, 1)    PRIMARY KEY,
    id_fornecedor  INTEGER  REFERENCES  seg_fornecedores(id),
    codigo_categoria INT       ,
    nome_categoria  VARCHAR(255)            NOT NULL


);
create unique index ix_seg_categorias_02 on seg_categorias (nome_categoria asc);
create unique index ix_seg_categorias_03 on seg_categorias (codigo_categoria asc);


