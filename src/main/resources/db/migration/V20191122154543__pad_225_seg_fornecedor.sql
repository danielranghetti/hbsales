create table seg_fornecedores
(
    id    INTEGER IDENTITY (1, 1)       PRIMARY KEY,
    razao_social VARCHAR(100)           NOT NULL,
    cnpj         VARCHAR(255)           NOT NULL,
    nome_fantasia VARCHAR(255)          NOT NULL,
    endereco     VARCHAR(36)            NOT NULL,
    telefone     VARCHAR(16)            NOT NULL,
    e_mail       VARCHAR(255)           NOT NULL




);

create unique index ix_seg_fornecedores_01 on seg_fornecedores (cnpj asc);
create unique index ix_seg_fornecedores_02 on seg_fornecedores (razao_social asc);
create unique index ix_seg_fornecedores_03 on seg_fornecedores (telefone asc);