# 🛡️ Auth Service - NV E-Commerce

Este projeto é um serviço de autenticação desenvolvido em Java com Spring Boot, responsável por gerenciar login, geração e renovação de tokens JWT, envio de e-mails e administração de refresh tokens. Ele faz parte da arquitetura do sistema **NV E-Commerce**.

---

## 🔧 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Security**
- **JWT (com Auth0 Java JWT)**
- **Spring Data JPA**
- **Spring Mail**
- **MySQL**
- **Lombok**
- **JUnit 5 & Mockito**
- **Swagger / OpenAPI**

---

## ✉️ Funcionalidade de Envio de E-mail

- Autenticação com e-mail e senha
- Geração de Access Token (JWT) e Refresh Token
- Renovação de tokens com validade
- Revogação de tokens individualmente ou em massa (logout global)
- Introspecção de tokens (validação de acesso e expiração)
- Proteção de rotas com base em roles: `ADMIN`, `GESTOR`, `CLIENTE`
- Envio automático de e-mail de **boas-vindas** após o cadastro de um novo usuário
- Uso de **template HTML** personalizado
- Configurável com qualquer SMTP (Gmail, Mailtrap, SendGrid, etc.)
- Serviço desacoplado para reuso em outras partes da aplicação

### 📄 Exemplo de Template HTML

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bem-vindo</title>
</head>
<body>
    <h1>Olá, {{nome}}!</h1>
    <p>Seja bem-vindo ao NV E-Commerce. Seu cadastro foi realizado com sucesso.</p>
</body>
</html>
```

---

## 🚀 Endpoints

### 🔐 Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/nv-ecommerce/auth/login` | Realiza login e retorna access + refresh token |
| `POST` | `/nv-ecommerce/auth/cadastrar` | Cadastro de novo usuário (a depender do contexto) |

---

### 🔄 Tokens

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/nv-ecommerce/auth/renovar-token` | Gera novo par de tokens baseado no refresh token |
| `DELETE` | `/nv-ecommerce/auth/revogar-token` | Revoga um único refresh token |
| `DELETE` | `/nv-ecommerce/auth/revogar-todos` | Revoga todos os refresh tokens do usuário logado |
| `GET` | `/nv-ecommerce/auth/introspect` | Valida o token enviado e retorna informações sobre o usuário e expiração |

---

## 📬 Configuração do E-mail

Adicione ao seu `application.properties` ou `application.yml`:

```properties
spring.mail.host=smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=seu_usuario
spring.mail.password=sua_senha
spring.mail.protocol=smtp
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## ✅ Testes

- Cobertura com **JUnit 5** e **Mockito**
- Testes de unidade para:
    - Introspecção de token
    - Renovação de token
    - Revogação individual e global
- Simulação de autenticação com `@WithMockUser`

---

## 📄 Exemplos de Requisições

### Cadastro com e-mail automático

```http
POST /nv-ecommerce/auth/cadastrar
Content-Type: application/json

{
  "nome": "João da Silva",
  "email": "joao@teste.com",
  "senha": "123456"
}
```

🔁 Após o cadastro:
- Um e-mail HTML é enviado automaticamente ao usuário.
- Tokens são retornados na resposta.

---

### Introspecção

```http
GET /nv-ecommerce/auth/introspect
Authorization: Bearer <access_token>

{
  "token": "<access_token>"
}
```

---

## 🛠️ Próximos passos

- Integração com OAuth2 (Google, GitHub)
- Armazenamento de tokens em Redis
- Blacklist persistente para tokens revogados
- Rate limiting para endpoints sensíveis
- Testes de integração

---

## 🧑‍💻 Desenvolvedor

**Nedson Vieira do Nascimento**

---