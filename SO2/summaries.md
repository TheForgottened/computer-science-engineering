## Deadlocks

Bloqueio permanente de dois ou mais processos.

### Condições Necessárias para Ocorrência de DeadLocks

1. **Exclusão Mútua**
2. **Detenção e Espera (Hold and Wait):** manter um recurso enquanto se espera pela libertação de outro
3. **Recursos Não Preemptíveis**
4. **Espera Circular**

## Grafos de Atribuição de Recursos

Ferramenta para descrição da situação de processos e recursos.

Se o grafo tiver ciclos:
- Pode existir um deadlock
- Se só existir uma unidade dos recursos envolvidos, então há um deadlock

Se o grafo não tiver ciclos:
- Não existem deadlocks

### Tratamento de Deadlocks

- **Prevenção / evitamento:** tentar impedir que ocorram deadlocks
- **Deteção e recuperação:** conseguir detetar e recuperar quando ocorrem
- **Ignorar:** assumir que nunca acontecem

Para prevenir basta fazer com que pelo menos uma das quatro condições necessárias ao deadlock nunca se verifique.

### Prevenção de Condições de Deadlock

#### Exclusão Mútua

Geralmente não é viável a prevenção de deadlocks pela negação desta condição.


#### Detenção e Espera

- Obrigar todos os processos a pedir todos os recursos que vão necessitar no início da execução
- Só permitir a atribuição de recursos a um processo se este não detiver nenhum no momento em que faz o pedido


#### Recursos não Preemptíveis

- Quando requisitar um recurso que não pode obter de imediato fica bloqueado à espera deste e perde também os que já tinha, tendo que ficar à espera destes também
- Quando requisitar um recurso, se este for detido por um processo bloqueado, o recurso passa para o processo que acabou de pedir, caso contrário o processo que fez o pedido fica bloqueado podendo perder alguns recursos que já detinha


#### Espera Circular

- Numerar os recursos por ordem crescente
- Permitir que o processo apenas obtenha um novo recurso se:
    - Estiver disponível
    - Tiver ordem superior a qualquer recurso detido pelo processo
    - O processo não detém recursos