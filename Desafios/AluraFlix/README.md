# 🎬 Alura Flix - Alura Challenger Backend 1

Este projeto é o resultado do **Desafio ALURA Flix** proposto na **Alura Challenger Backend 1**. Ele consiste no desenvolvimento de uma plataforma para compartilhamento de vídeos, onde os usuários podem montar playlists com links para seus vídeos preferidos, organizados por categorias.

---

## 📋 Índice

- [Descrição](#descrição)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura da API](#estrutura-da-api)
- [Testes](#testes)
- [Autenticação e Autorização](#autenticação-e-autorização)
- [Como Executar o Projeto](#como-executar-o-projeto)
- [Documentação da API](#documentação-da-api)
- [Boas Práticas](#boas-práticas)

---

## 📖 Descrição

O **ALURA Flix** é uma plataforma para compartilhamento de vídeos que permite aos usuários:

- 📺 Compartilhar links para seus vídeos preferidos.
- 🎵 Organizar vídeos em playlists, separados por categorias.
- 🔄 Gerenciar vídeos, categorias e usuários por meio de operações CRUD (criação, leitura, atualização e exclusão).

---

## ⚙️ Funcionalidades

- **API REST:** Rotas implementadas seguindo os padrões REST.
- **CRUD Completo:** Gerenciamento de vídeos, categorias e usuários.
- **Validações:** Regras de negócio aplicadas via anotações e validações customizadas.
- **Paginação:** Listagens de vídeos e categorias com suporte à paginação.
- **Persistência de Dados:** Utilização de banco de dados relacional para armazenamento das informações.
- **Testes Automatizados:** Testes unitários e de integração para controllers, services, models e repositórios.
- **Segurança:** Serviço de autenticação e autorização com JWT para acesso seguro às rotas.
- **Documentação OpenAPI:** Documentação interativa dos endpoints.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem & Build:**  
  - ☕ Java com Maven

- **Frameworks e Bibliotecas:**  
  - 🌱 Spring Boot  
    - 🔑 Spring Security (com Java JWT para autenticação)  
    - 📖 Spring Doc Openapi (documentação da API)  
    - 🗂️ Spring Data JDBC (interação com o banco de dados)  
  - 🛤️ FlywayDB (migração de banco de dados)

- **Banco de Dados:**  
  - 🐬 MySQL

- **Ferramentas de Teste e Desenvolvimento:**  
  - 🧰 Postman (testes de API)

---

## 🏗️ Estrutura da API

A API foi desenvolvida seguindo boas práticas e possui os seguintes módulos:

- **Vídeos:** Endpoints para cadastro, listagem, atualização, busca por ID e exclusão (inativação) de vídeos.
- **Categorias:** CRUD para gerenciar categorias de vídeos.
- **Usuários:** Gerenciamento de usuários (autenticação e cadastro).

Cada módulo é organizado em:
- **Controllers:** Mapeamento das rotas e comunicação com os serviços.
- **Services:** Regras de negócio.
- **Repositories:** Operações de persistência no banco.
- **Models/Entities:** Representação dos dados.
- **Records (Dados...):** DTOs imutáveis para entrada e saída de dados.

---

## ✅ Testes

O projeto conta com uma sólida cobertura de testes:
- **Testes Unitários:** Para controllers, services, models e validações.
- **Testes de Integração:** Para validar a integração entre as camadas (controller, service e repository).

Ferramentas utilizadas:
- **JUnit 5**
- **Mockito**
- **AssertJ**
- **JacksonTester**

Os testes estão organizados em pacotes separados para facilitar a manutenção e a execução independente.

---

## 🔒 Autenticação e Autorização

A API utiliza **Spring Security** e **Java JWT** para:
- Proteger as rotas (GET, POST, PUT e DELETE).
- Implementar autenticação de usuários.
- Controlar acesso, garantindo que apenas usuários autorizados possam acessar os recursos.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- JDK 17 ou superior
- Maven
- MySQL (configurar o banco conforme as propriedades em `application.properties`)
- Flyway para migração do banco de dados

### Passos
1. **Clone o repositório:**
   ```bash
   git clone https://github.com/nedsonvieira/Projetos-Spring/tree/main/Desafios/AluraFlix
   cd AluraFlix
   ```

2. **Configure o Banco de Dados:**

 - Certifique-se de que o MySQL esteja instalado e em execução.
 - Crie o banco de dados e configure as credenciais no arquivo `application.properties`.

3. **Execute as Migrações:**
  ```bash
  mvn flyway:migrate
  ```

4. **Compile e Execute a aplicação**
  ```bash
  mvn clean install
  mvn spring-boot:run
  ```

##🗂️ Documentação da API

Disponível em: `http://localhost:8080/swagger-ui.html`

## ✨Boas Práticas
O projeto foi desenvolvido com foco em:

Separação de responsabilidades: Controllers, services, repositories e models organizados em pacotes distintos.
Validações robustas: Regras de negócio implementadas com validações via Jakarta Validation e lógicas customizadas.
Testabilidade: Cobertura de testes unitários e de integração.
Segurança: Implementação de autenticação e autorização para proteger os endpoints.
Documentação: Uso de OpenAPI para documentação clara e interativa da API.

### 🤝 Contribuições
Contribuições são bem-vindas! Sinta-se à vontade para abrir um issue ou enviar um Pull Request.

###### 🌟 Desenvolvido como parte do Desafio ALURA Flix na Alura Challenger 1. 🚀
