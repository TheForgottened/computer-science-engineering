# Checklist para Meta 2

## Atualização da BD e disseminação da informação

- [x] Mecanismos de obtenção de informação por parte dos clientes: 
    1. Cliente envia pedido ao servidor via TCP
    2. Servidor submete query ao SGBD
    3. Servidor devolve resultado ao cliente via TCP
- [x] Adoção do mecanismo sistemático indicado no enunciado quando é feita uma alteração à BD por parte de um servidor na sequência de um pedido de um cliente: 
    1. Cliente solicita operação ao servidor via TCP
    2. Servidor submete update(s) correspondente(s) ao SGBD (INSERT, UPDATE ou DELETE)
    3. Servidor envia informação de alteração da BD ao GRDS e devolve resultado ao cliente
    4. O GRDS reencaminha a informação recebida pelos servidores ativos
    5. Servidores notificam os seus clientes (todos ou, idealmente, os que são impactados pela alteração) que houve uma alteração na BD
    6. Os clientes solicitam aos seus servidores a informação necessária para apresentar nas suas vistas (mecanismo de obtenção de informação anterior)
    7. Clientes atualizam as suas vistas/informação apresentada aos utilizadores

---

- [x] Thread dedicada a receber datagramas UDP no GRDS (eventualmente uma segunda no multicast socket se este for usado apenas para efeitos de descoberta)

---

- [x] Thread dedicada nos servidores para receção de pedidos de ligação pelos clientes via TCP
- [x] Thread nos servidores para cada cliente ligado via TCP
- [x] Thread nos servidores para receção de datagramas UDP enviados pelo GRDS
- [x] Thread nos clientes dedicada à receção de notificações assíncronas (TCP)
- [x] (PARCIALMENTE) Ligações TCP temporárias (e threads adicionais associadas) para transferência de ficheiros em background entre servidores
- [x] Mecanismos de atualização periódica/revalidação na BD do estado online dos clientes conectados e autenticados 
- [x] Thread nos servidores dedicada ao envio periódico de heartbeats via UDP para o GRDS


## Funcionalidade

- [x] (PARCIALMENTE) Ligação de um servidor ao SGBD e contacto com o GRDS (unicast ou multicast)
- [x] Ligação de um cliente a um servidor via GRDS (unicast)
- [x] Registo (nome + username + password)
- [x] Autenticação (nome + username)
- [x] Edição dos dados de registo
- [x] Listagem e pesquisa de utilizadores
- [x] Estabelecimento de contacto com outro utilizador (3 fases: pedido, notificação destinatário e aceitação/recusa)
- [x] Listagem de contactos (com informação sobre estado online/offline)
- [x] Eliminação de contacto -> efeito nos 2 utilizadores e eliminação das mensagens e notificações associadas
- [x] Criação de grupos
- [x] Alteração do nome de um grupo (administrador)
- [x] Eliminação de um membro de um grupo (administrador)
- [x] Eliminação de um grupo (administrador) -> eliminação das mensagens/notificações associadas em todos os membros e dos ficheiros nos servidores
- [x] Listagem de grupos existentes (nome + membros)
- [x] Adesão a um grupo (3 fases: pedido + notificação administrador + aceitação/recusa)
- [x] Saída de um grupo
- [x] Envio de mensagens / notificações de disponibilização de ficheiros para contactos
- [x] (PARCIALMENTE) Listagem das mensagens / notificações trocadas (com informação do momento e se já foi visto por pelo menos um dos destinatários)
- [x] (PARCIALMENTE) Obtenção dos ficheiros disponibilizados
- [x] Gestão de ficheiros com nomes idênticos nos servidores
- [x] Eliminação de uma mensagem / notificação do histórico
- [x] Eliminação do ficheiro associado a uma notificação em todos os servidores quando esta é eliminada pelo emissor
- [x] Ligação automática a outro servidor quando a ligação ao atual vai abaixo (volta a contactar o GRDS, etc.)
- [x] Atualização assíncrona das vistas / notificações assíncronas nas vistas

---

- [x] Extra GUI
- [x] (PARCIALMENTE) Extra consistência estrita réplicas dos ficheiros

