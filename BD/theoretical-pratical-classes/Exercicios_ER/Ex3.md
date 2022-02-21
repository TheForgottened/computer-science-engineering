**Relacionamento Rel_A_B**

1:N, N obrigatório -> entidade fraca
-> 2 tabelas

- Entidade A <<u>A1</u>, A2>
- Entidade B <<u>A1, B1</u>, B2, B3>, A1 FK Entidade A **(2)**

**Relacionamento Rel_A_C1**

1:N, N não obrigatório + 1 atributo multivalor
-> 3 tabelas

- Entidade A <<u>A1</u>, A2>
- Entidade C <<u>C1</u>, C2>
    - Multivalor <<u>C1, C3</u>> C1 FK Entidade C
- Rel_A_C1 <<u>A1, C1</u>> A1 FK Entidade A, C1 FK Entidade C **(5)**

**Relacionamento Rel_A_C2**

M:N + 1 atributo multivalor
-> 4 tabelas

- Entidade A <<u>A1</u>, A2> **(1)**
- Entidade C <<u>C1</u>, C2>
    - Multivalor <<u>C1, C3</u>> C1 FK Entidade C
- Rel_A_C2 <<u>A1, C1</u>> A1 FK Entidade A, C1 FK Entidade C **(6)**

**Relacionamento Rel_C_D**

M:N + 1 atributo multivalor
-> 4 tabelas

- Entidade C <<u>C1</u>, C2> **(3)**
    - Multivalor <<u>C1, C3</u>> C1 FK Entidade C **(4)**
- Entidade D <<u>D1</u>, D3, D4, D5> **(7)**
- Rel_C_D <<u>C1, D1</u>, X> C1 FK Entidade C, D1 FK Entidade D **(8)**

**TABELAS FINAIS**

Da **(1)** à **(8)**.