ALTER TABLE seg_produtos ADD CONSTRAINT Fk_seg_produtos_FGK FOREIGN KEY (id_linha_categoria) REFERENCES seg_linha_categorias(id);