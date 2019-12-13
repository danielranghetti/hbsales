ALTER TABLE "dbo"."seg_categorias"
ALTER COLUMN  codigo_categoria VARCHAR(10) NOT NULL ;
ALTER TABLE "dbo"."seg_categorias"
    ADD UNIQUE ("id_fornecedor", "codigo_categoria");