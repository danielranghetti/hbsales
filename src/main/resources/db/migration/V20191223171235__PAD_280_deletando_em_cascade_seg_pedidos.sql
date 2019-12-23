ALTER TABLE "dbo"."seg_itens"
	ADD CONSTRAINT "FK1" FOREIGN KEY ("id_pedido") REFERENCES "seg_pedidos" (id) ON DELETE CASCADE;