ALTER TABLE "dbo"."seg_categorias"
    ADD CONSTRAINT unique_fornecedor_codigo UNIQUE ("id_fornecedor", "codigo_categoria");
