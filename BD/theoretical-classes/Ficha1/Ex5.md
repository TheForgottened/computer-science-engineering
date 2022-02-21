### **Modelo Físico ER:**

**Relacionamento Rel_A_D:**

- 1:N, com N obrigatorio -> 2 tabelas
- ENTIDADE D <<span style="text-decoration: underline">D1</span>, D3, D4>
- ENTIDADE A <<span style="text-decoration: underline">A1, A2</span>, A3, D1> D1 FK ENTIDADE D

**Relacionamento Rel_A_C1:**

- M:N -> 3 tabelas
- ENTIDADE A <<span style="text-decoration: underline">A1, A2</span>, A3, D1> D1 FK ENTIDADE D
- ENTIDADE C <<span style="text-decoration: underline">C1</span>, C2> 
    - TABELA C3 <<span style="text-decoration: underline">C1, C3</span>> C1 FK ENTIDADE C
- REL_A_C1 <<span style="text-decoration: underline">C1, A1, A2</span>, Y> C1 FK ENTIDADE C; A1+A2 FK ENTIDADE A (7)

**Relacionamento Rel_A_C2:**

- 1:1, com 1 obrigatório -> 2 tabelas
- ENTIDADE C <<span style="text-decoration: underline">C1</span>, C2> 
    - TABELA C3 <<span style="text-decoration: underline">C1, C3</span>> C1 FK ENTIDADE C
- ENTIDADE A <<span style="text-decoration: underline">A1, A2</span>, A3, D1, C1> D1 FK ENTIDADE D; C1 FK ENTIDADE C

**Relacionamento Rel_A_C_D:**

- M:N:P -> 4 tabelas + 1 (atributo multivalor)
- ENTIDADE A <<span style="text-decoration: underline">A1, A2</span>, A3, D1, C1> D1 FK ENTIDADE D; C1 FK ENTIDADE C (5)
- ENTIDADE C <<span style="text-decoration: underline">C1</span>, C2> (3)
    - TABELA C3 <<span style="text-decoration: underline">C1, C3</span>> C1 FK ENTIDADE C (4)
- ENTIDADE D <<span style="text-decoration: underline">D1</span>, D3, D4> 
- REL_A_C_D <<span style="text-decoration: underline">A1, A2, C1, D1, X</span>> A1+A2 FK ENTIDADE A; C1 FK ENTIDADE C; D1 FK ENTIDADE D (6)

**Relacionamento Rel_D_B:**

- 1:N, com N obrigatório e relacionamento fraco
- ENTIDADE D <<span style="text-decoration: underline">D1</span>, D3, D4> (1)
- ENTIDADE B <<span style="text-decoration: underline">D1, B1</span>, B2> D1 FK ENTIDADE D (2)

**Tabelas finais:**

Da (1) à (7).