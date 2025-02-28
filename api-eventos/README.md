# Projeto desenvolvido no evento NLW - Connect
###### - Organização: Rocketseat
###### - Trilha: Java

## Sistema de Inscrições em Eventos

Este é um sistema para gestão de inscrições em eventos, permitindo que usuários se inscrevam, gerem links de indicação e visualizem rankings de indicações.

## Requisitos Funcionais

- **Inscrição**: O usuário pode se inscrever no evento usando nome e e-mail.
- **Geração de Link de Indicação**: O usuário pode gerar um link de indicação.
- **Ranking de Indicações**: O usuário pode ver o ranking de indicações.
- **Visualização de Indicações**: O usuário pode ver a quantidade de inscritos que ingressaram com seu link.

## User Stories

### US00 - CRUD de Evento

Endpoints:

- **Criar um novo evento**
  - `POST /eventos`
  - **Requisição:**
    ```json
    {
      "nome": "CodeCraft Summit 2025",
      "local": "Online",
      "preco": 0.0,
      "dataInicio": "2025-03-16",
      "dataFim": "2025-03-18",
      "horaInicio": "19:00:00",
      "horaFim": "21:00:00"
    }
    ```
  - **Resposta:**
    ```json
    {
      "id": 1,
      "nome": "CodeCraft Summit 2025",
      "prettyName": "codecraft-summit-2025",
      "local": "Online",
      "preco": 0.0,
      "dataInicio": "2025-03-16",
      "dataFim": "2025-03-18",
      "horaInicio": "19:00:00",
      "horaFim": "21:00:00"
    }
    ```

- **Listar eventos**
  - `GET /eventos`
  - **Resposta:**
    ```json
    [
      {
      	"id": 1,
      	"nome": "CodeCraft Summit 2025",
      	"prettyName": "codecraft-summit-2025",
      	"local": "Online",
      	"preco": 0.0,
      	"dataInicio": "2025-03-16",
      	"dataFim": "2025-03-18",
      	"horaInicio": "19:00:00",
      	"horaFim": "21:00:00"
      }
    ]
    ```

- **Recuperar evento pelo Pretty Name**
  - `GET /eventos/{prettyName}`

### US01 - Realizar Inscrição

- `POST /inscricao/{prettyName}`
  - **Requisição:**
    ```json
    {
      "nome": "John Doe",
      "email": "john@doe.com"
    }
    ```
  - **Resposta:**
    ```json
    {
      "numeroInscricao": 1,
      "designicao": "https://devstage.com/codecraft-summit-2025/123"
    }
    ```

### US02 - Gerar Ranking de Inscritos

- `GET /inscricao/{prettyName}/ranking`
- **Resposta:**
  ```json
[
	{
        "inscritos": 3,
        "usuarioId": 5,
        "nome": "John Doe"
    },
    {
        "inscritos": 2,
        "usuarioId": 6,
        "nome": "Larry Loe"
    }
]
  ```

### US03 - Estatísticas de Inscritos por Participante

- `GET /inscricao/{prettyName}/ranking/{usuarioId}`
- **Resposta:**
  ```json
  {
    "item": {
        "inscritos": 1,
        "usuarioId": 7,
        "nome": "Mary May"
    },
    "posicao": 4
}
  ```