create table seg_periodo_venda
(
    id    INTEGER IDENTITY (1, 1)       PRIMARY KEY,
    id_fornecedor           INTEGER  REFERENCES  seg_fornecedores(id) NOT NULL,
    data_inicial            DATE NOT NULL,
    data_final              DATE NOT NULL,
    data_retirada           DATE NOT NULL,
    );