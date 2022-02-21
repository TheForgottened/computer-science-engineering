## 1. b)

1º caso de uso:

| bruh | Descrição |
| --- | --- |
| Título | Regista compras |
| Descrição | O operador regista as compras do cliente, recebe o pagamento, devolve o troco e a fatura, o sistema regista a operação |
| Sistema | Point-of-Sale |
| Ator Primário | Operador | 
| Participantes | Base de Dados |
| Objetivos | Operador vai ter compra associada a si, O cliente adquire o produto, e recebe fatura (declarar IRS, possibilita troca), a Base de Dados | 
| Pré-condição | Ligação à base de dados tem de estar ativa |
| Pós-condição | Transação fica registada, o cliente recebe fatura, compra fica registada |
| Fluxo de Eventos | 1. Sistema apresenta mensagem de boas vindas <br> 2. Sistema reconhece as credenciais do Operador usando o caso de uso "Introduz credenciais" <br> 3. Operador introduz a identificação do produto e o número de unidades <br> 4. Sistema apresenta informações sobre o produto, o preço unitário e o subtotal, e o total até ao momento <br> 5. Enquanto existirem mais produtos a introduzir, volta para o ponto 3 <br> 6. Operador indica que terminou a introdução de produtos <br> 7. O sistema apresenta o total a pagar <br> 8. Operador introduz quantia paga pelo cliente <br> 9. Sistema indica o troco a devolver <br> 10. Sistema imprime fatura <br> 11. Sistema regista transação na base de dados <br><br> <u>Cenários Alternativos</u> <br> 2. a) Sistema não reconheceu as credenciais do Operador <br> &emsp; 1. Sistema avisa da situação e pergunta ao utilizador se quer tentar de novo ou sair <br> &emsp; 1.1. Operador escolhe tentar de novo. Continua no ponto 2. <br> &emsp; 1.2. Operador escolhe desistir. Caso de uso termina em falha. <br><br> 3. a) Sistema não reconhece o produto <br> &emsp; 1. Sistema avisa do erro e volta para o ponto 3 <br><br> 3-7. a) Operador decide que afinal não quer registar um produto <br> &emsp; 1. Operador indica identificação do produto e o número de unidades <br> &emsp; 2. Sistema recalcula o total <br> &emsp; 3. Volta para o ponto 5  <br><br> 10. a) Fatura não consegue ser impressa <br> &emsp; 1. Sistema avisa do erro e sugere o preenchimento manual da fatura <br> &emsp; 2. Volta para o ponto 11 <br><br> 11. a) Ligação à base de dados falha <br> &emsp; 1. Sistema avisa do erro <br> &emsp; 2. Volta ao ponto 11 |

---

2º caso de uso:

| bruh | Descrição |
| --- | --- |
| Título | Introduz credenciais |
| Descrição | O operador introduz as credenciais para serem verificadas pelo sistema |
| Sistema | Point-of-Sale |
| Ator Primário | Operador | 
| Participantes | N/A |
| Objetivos | Reconhecer se o operador tem as credenciais necessárias (para aceder a determinada funcionalidade) | 
| Pré-condição | N/A |
| Pós-condição | O operador passa a ser reconhecido como tendo credenciais válidas |
| Fluxo de Eventos | 1. Sistema pede o código de acesso <br> 2. Sistema verifica que o código é válido <br> 3. As credenciais são reconhecidas como válidas <br><br> <u>Cenário Alternativo</u> <br> 2. a) O sistema não reconhece o código <br> &emsp; 1. As credenciais não são reconhecidas como válidas. O caso de uso falhou |

---

3º caso de uso:

| bruh | Descrição |
| --- | --- |
| Título | Devolve produto |
| Descrição | Operador indica o produto a devolver, o sistema indica a quantia a entregar ao cliente, e emite uma fatura da devolução |
| Sistema | Point-of-Sale |
| Ator Primário | Operador | 
| Participantes | N/A |
| Objetivos | Operador vai ter a devolução associada a si, O cliente devolve o produto e recebe a quantia correspondente ao produto e recebe fatura atualizada (declarar IRS, possibilita troca), a Bases de Dados regista a transação | 
| Pré-condição | Ligação à base de dados tem de estar ativa, tem de existir fatura |
| Pós-condição | Operador vai ter a devolução associada a si, Cliente recebe o dinheiro e recebe fatura (declarar IRS, possibilita troca), Base de Dados regista a transação |
| Fluxo de Eventos | 1. Sistema reconhece as credenciais do Operador usando o caso de uso "Introduz credenciais" <br> 2. Sistema pede identificação da fatura <br> 3. Operador introduz os dados da fatura <br> 4. Sistem reconhece a fatura como válida <br> 5. Operador introduz a identificação do produto <br> 6. Sistema indica o valor a devolver <br> 7. Sistema emite a fatura <br> 8. Base de Dados regista a transação <br><br> <u>Cenário Alternativo</u> <br> 1. a) Sistema não reconheceu as credenciais do Operador <br> &emsp; 1. Sistema avisa da situação e pergunta ao utilizador se quer tentar de novo ou sair <br> &emsp; 1.1. Operador escolhe tentar de novo. Continua no ponto 2. <br> &emsp; 1.2. Operador escolhe desistir. Caso de uso termina em falha. <br><br> 4. a) Sistema não reconhece identificação da fatura <br> &emsp; 1. Sistema avisa e volta ao ponto 3 <br><br> 4. b) Fatura é reconhecida, mas já passou o período de trocas <br> &emsp; 1. Sistema avisa <br> &emsp; 2. Caso de uso termina em falha <br><br> 6. a) Operador deseja devolver mais produtos <br> &emsp; 1. Volta ao ponto 5 <br><br> 6. b) Sistema não reconhece o produto <br> &emsp; 1. Não é indicado valor a devolver <br> &emsp; 2. Volta ao ponto 5 |