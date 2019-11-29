create table seg_linha_categorias
(
    id    BIGINT IDENTITY (1, 1)    PRIMARY KEY,
    id_categoria  BIGINT   REFERENCES  seg_categorias(id) NOT NULL,
    cod_linha_categoria_ INTEGER      NOT NULL,
    nome_linha  VARCHAR(255)            NOT NULL


);