### **Modelo Físico ER:**

**Relacionamento efetua:**

- 1:N, N obrigatório -> 2 tabelas
- COMPANHIA <<span style="text-decoration: underline">siglac</span>, nome, navioes>
- VOO <<span style="text-decoration: underline">siglav</span>, tipo, siglac> siglac FK COMPANHIA

**Relacionamento agências:**

- 1:N, N obrigatório -> 2 tabelas
- COMPANHIA <<span style="text-decoration: underline">siglac</span>, nome, navioes> (2)
- ESCRITORIO <<span style="text-decoration: underline">cod_esc</span>, rua, numero, siglac> siglac FK COMPANHIA

**Relacionamento local:**

- 1:N, N obrigatório -> 2 tabelas
- CIDADE <<span style="text-decoration: underline">cod_cid</span>, designacao, pais>
- ESCRITORIO <<span style="text-decoration: underline">cod_esc</span>, rua, numero, siglac, cod_cid> siglac FK COMPANHIA; cod_cid FK CIDADE (3)

**Relacionamento partida:**

- 1:N, N obrigatório -> 2 tabelas
- CIDADE <<span style="text-decoration: underline">cod_cid</span>, designacao, pais>
- VOO <<span style="text-decoration: underline">siglav</span>, tipo, siglac, cod_cid> siglac FK COMPANHIA; cod_cid FK CIDADE

**Relacionamento chegada:**

- 1:N, N obrigatório -> 2 tabelas
- CIDADE <<span style="text-decoration: underline">cod_cid</span>, designacao, pais> (1)
- VOO <<span style="text-decoration: underline">siglav</span>, tipo, siglac, cod_cid_p, cod_cid_c> siglac FK COMPANHIA; cod_cid_p FK CIDADE; cod_cid_c FK CIDADE

**Relacionamento escala:**

- M:N -> 3 tabelas
- VOO <<span style="text-decoration: underline">siglav</span>, tipo, siglac, cod_cid_p, cod_cid_c> siglac FK COMPANHIA; cod_cid_p FK CIDADE; cod_cid_c FK CIDADE (4)
- CIDADE <<span style="text-decoration: underline">cod_cid</span>, designacao, pais>
- ESCALA <<span style="text-decoration: underline">siglav</span>, cod_c, tempo> siglav FK VOO; cod_c FK CIDADE (5)

**Tabelas finais:**

Da (1) à (5).