ALTER TABLE "dbo"."seg_produtos" ALTER COLUMN  cod_produto VARCHAR(10) NOT NULL;
ALTER TABLE "dbo"."seg_produtos" ADD UNIQUE ("cod_produto");
ALTER TABLE "dbo"."seg_produtos"  ALTER COLUMN  nome VARCHAR(200) NOT NULL;
ALTER TABLE "dbo"."seg_produtos" ADD  unidade_medida VARCHAR(2) NOT NULL;