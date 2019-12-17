ALTER TABLE seg_pedidos ALTER COLUMN id_funcionario BIGINT NOT NULL;
ALTER TABLE seg_pedidos ADD CONSTRAINT Fk_seg_pedidos_fun_FGK FOREIGN KEY (id_funcionario) REFERENCES seg_funcionarios(id);