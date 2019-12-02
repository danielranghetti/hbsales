create table seg_produtos
(
    id    INTEGER IDENTITY (1, 1)       PRIMARY KEY,
    id_linha_categoria BIGINT  REFERENCES  seg_categorias(id) NOT NULL,
    cod_produto INTEGER           NOT NULL,
    nome         VARCHAR(255)           NOT NULL,
    preco        DECIMAL(25,2)          NOT NULL,
    uni_caixa    INTEGER            NOT NULL,
    peso_uni     DECIMAL(25,3)            NOT NULL,
    validade       DATE           NOT NULL




);