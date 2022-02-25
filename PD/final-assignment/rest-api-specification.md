# MetaPD RESTAPI: Documento de Especificação

## GET

### Lista de grupos que o utilizador pertence

URI: /groups/list

#### Request Parameters

| Header | Body |
|--------|:----:|
| Bearer Token | - |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Tipo incorreto de token | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | <pre lang="json">[<br>    {<br>        "id": 11,<br>        "name": "defesa",<br>        "members": [<br>            {<br>                "username": "jm",<br>                "name": "Jose Marinho",<br>                "state": false<br>            },<br>            {<br>                "username": "testuser1",<br>                "name": "bruh",<br>                "state": false<br>            },<br>            {<br>                "username": "testuser2",<br>                "name": "novo nome",<br>                "state": false<br>            }<br>        ]<br>    }<br>]</pre> |


### Todas as mensagens de grupo do utilizador

URI: /groups/all-messages

#### Request Parameters

| Header | Body |
|--------|:----:|
| Bearer Token | - |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Tipo incorreto de token | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | <pre lang="json">[<br>    {<br>        "id": 34,<br>        "senderUsername": "jm",<br>        "text": "uma mensagem de groupo",<br>        "fileName": null,<br>        "sendDate": "2022-01-05T15:26:53.000+00:00",<br>        "groupName": "defesa",<br>        "groupMessage": true<br>    },<br>    {<br>        "id": 37,<br>        "senderUsername": "testuser2",<br>        "text": "uma mensagem 2",<br>        "fileName": null,<br>        "sendDate": "2022-01-05T15:41:20.000+00:00",<br>        "groupName": "defesa2",<br>        "groupMessage": true<br>    }<br>]</pre> |


### Todas as mensagens de um grupo do utilizador

URI: /groups/messages

#### Request Parameters

| Header | Body |
|--------|:----:|
| Bearer Token <br> int groupId | - |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Tipo incorreto de token | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | <pre lang="json">[<br>    {<br>        "id": 34,<br>        "senderUsername": "jm",<br>        "text": "uma mensagem de groupo",<br>        "fileName": null,<br>        "sendDate": "2022-01-05T15:26:53.000+00:00",<br>        "groupName": "defesa",<br>        "groupMessage": true<br>    }<br>]</pre> |


### Lista de contactos do utilizador

URI: /contacts/list

#### Request Parameters

| Header | Body |
|--------|:----:|
| Bearer Token | - |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Tipo incorreto de token | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | <pre lang="json">[<br>    {<br>        "username": "testuser2",<br>        "name": "novo nome",<br>        "state": false<br>    }<br>]</pre> |


### Todas as mensagens privadas do utilizador

URI: /contacts/all-messages

#### Request Parameters

| Header | Body |
|--------|:----:|
| Bearer Token | - |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Tipo incorreto de token | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | <pre lang="json">[<br>    {<br>        "id": 1,<br>        "senderUsername": "testuser1",<br>        "text": "bruh test",<br>        "fileName": null,<br>        "sendDate": "2021-12-21T00:51:08.000+00:00",<br>        "groupName": "",<br>        "groupMessage": false<br>    },<br>    {<br>        "id": 12,<br>        "senderUsername": "testuser1",<br>        "text": "dasdfasdf\nasdfasdf",<br>        "fileName": null,<br>        "sendDate": "2022-01-02T22:54:12.000+00:00",<br>        "groupName": "",<br>        "groupMessage": false<br>    }<br>]</pre> |


### Todas as mensagens de um contacto do utilizador

URI: /contacts/messages

#### Request Parameters

| Header | Body |
|--------|:----:|
| Bearer Token <br> String contactUsername | - |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Body do pedido vazio ou tipo incorreto de token | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | <pre lang="json">[<br>    {<br>        "id": 1,<br>        "senderUsername": "testuser1",<br>        "text": "bruh test",<br>        "fileName": null,<br>        "sendDate": "2021-12-21T00:51:08.000+00:00",<br>        "groupName": "",<br>        "groupMessage": false<br>    },<br>    {<br>        "id": 12,<br>        "senderUsername": "testuser1",<br>        "text": "dasdfasdf\nasdfasdf",<br>        "fileName": null,<br>        "sendDate": "2022-01-02T22:54:12.000+00:00",<br>        "groupName": "",<br>        "groupMessage": false<br>    }<br>]</pre> |


---

## POST

### Gerar um novo token

URI: /session

#### Request Parameters

| Header | Body |
|--------|------|
| - | <pre lang="json">{<br>    "username": "username_here",<br>    "password": "password_here"<br>}</pre> |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Body do pedido vazio | - |
| 401 | Pedido não autorizado, credenciais de login inválidas | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | e17b700dd96d231f70d024cb31ceef96 |


---

## PUT

### Alterar o nome do utilizador

URI: /profile/new-name

#### Request Parameters

| Header | Body |
|--------|------|
| Bearer Token | <pre>new name here</pre> |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Body do pedido vazio ou tipo incorreto de token | - |
| 401 | Pedido não autorizado, credenciais de login inválidas | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | - |


---

## DELETE

### Remover um contacto do utilizador

URI: /contacts/remove

#### Request Parameters

| Header | Body |
|--------|------|
| Bearer Token | <pre>contact username here</pre> |

#### Response

| HTTP Status | Significado | Exemplo |
|:-----------:|-------------|---------|
| 400 | Body do pedido vazio ou tipo incorreto de token | - |
| 401 | Pedido não autorizado, credenciais de login inválidas | - |
| 500 | Não foi possível comunicar com a BD | - |
| 200 | Sucesso | - |


## Autores

### Ângelo Paiva

Licenciatura em Engenharia Informática - Ramo de Desenvolvimento de Aplicações

a2019129023@isec.pt


### Daniel Ribeiro

Licenciatura em Engenharia Informática - Ramo de Desenvolvimento de Aplicações

a21270945@isec.pt


### Francisco Ferreira

Licenciatura em Engenharia Informática - Ramo de Desenvolvimento de Aplicações

a21250179@isec.pt


###### Rendered with md2pdf @ https://md2pdf.netlify.app/