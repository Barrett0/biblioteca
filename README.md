# 📚 Sistema de Gerenciamento de Biblioteca

API REST desenvolvida em **Java 21** e **Spring Boot** para gerenciamento completo de autores, livros, usuários e empréstimos em uma biblioteca.

O projeto foi construído seguindo boas práticas de arquitetura em camadas, validação de entradas, tratamento global de exceções, persistência de dados com JPA/Hibernate, documentação interativa via Swagger/OpenAPI e uma suíte completa de testes automatizados com JUnit 5 e Mockito.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 4.1.x
- **Persistência:** Spring Data JPA / Hibernate
- **Banco de Dados:** H2 Database (em memória)
- **Validação:** Jakarta Bean Validation (Hibernate Validator)
- **Documentação de API:** Springdoc OpenAPI / Swagger UI
- **Gerenciador de Dependências:** Apache Maven (com Maven Wrapper)
- **Testes Automatizados:** JUnit 5, Mockito e Spring Boot Test

---

## 📋 Funcionalidades e Regras de Negócio

### Autores
- Cadastro, listagem, busca por ID, atualização e exclusão de autores.
- Validação de e-mail único por autor.

### Livros
- Cadastro, listagem, busca por ID, atualização e exclusão de livros.
- Associação obrigatória a um autor previamente cadastrado.
- Validação de ISBN único por livro e ano de publicação positivo.

### Usuários
- Cadastro, listagem, busca por ID, atualização e exclusão de usuários da biblioteca.
- Validação de e-mail único por usuário.

### Empréstimos e Devoluções
- **Prazo de devolução:** Calculado automaticamente para **7 dias** após a data de empréstimo.
- **Controle de disponibilidade:** Um livro não pode ser emprestado caso já esteja em posse de outro usuário (com empréstimo ativo).
- **Limite por usuário:** Cada usuário pode ter no máximo **3 empréstimos ativos simultâneos**.
- **Devolução:** Atualiza a data de devolução para a data atual e libera o livro para novos empréstimos.
- **Bloqueio de devolução duplicada:** Não é permitido devolver um livro que já foi devolvido.
- **Consultas especializadas:** Listagem de todos os empréstimos, empréstimos ativos, empréstimos atrasados, histórico completo por usuário e empréstimos ativos por usuário.

### Tratamento Global de Exceções
- Respostas padronizadas com código HTTP, mensagem descritiva, lista de violações de validação e timestamp (`GlobalExceptionHandler`).

---

## ⚙️ Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:
- **[JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)** (ou superior) configurado na variável de ambiente `JAVA_HOME`.
- **[Git](https://git-scm.com/)** para clonar o repositório.
*(Não é obrigatório ter o Maven instalado globalmente, pois o projeto inclui o Maven Wrapper).*

---

## 🚀 Como Executar o Projeto

### 1. Clonar o repositório
```bash
git clone https://github.com/seu-usuario/biblioteca.git
cd biblioteca
```

### 2. Executar via Terminal (Maven Wrapper)

- **No Windows (PowerShell / CMD):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

- **No Linux / macOS:**
  ```bash
  chmod +x ./mvnw
  ./mvnw spring-boot:run
  ```

A aplicação iniciará na porta padrão `8080` (acesse em `http://localhost:8080`).

---

### 3. Executar via IDE

<details>
<summary><b>Eclipse IDE</b></summary>

1. Abra o Eclipse e selecione `File` > `Import...`.
2. Escolha `Maven` > `Existing Maven Projects` e selecione a pasta raiz do projeto.
3. Aguarde o download das dependências.
4. Localize a classe `src/main/java/com/biblioteca/biblioteca/BibliotecaApplication.java`.
5. Clique com o botão direito e selecione `Run As` > `Java Application`.
</details>

<details>
<summary><b>IntelliJ IDEA</b></summary>

1. Abra o IntelliJ e selecione `Open...`.
2. Selecione o arquivo `pom.xml` ou a pasta do projeto e escolha **Open as Project**.
3. Aguarde a sincronização das dependências do Maven.
4. Execute o arquivo `BibliotecaApplication.java` clicando no botão de play verde ao lado da classe.
</details>

<details>
<summary><b>VS Code</b></summary>

1. Instale a extensão **Extension Pack for Java**.
2. Abra a pasta do projeto no VS Code.
3. Abra `BibliotecaApplication.java` e clique em **Run** acima da função `main`.
</details>

---

## 📖 Documentação da API (Swagger / OpenAPI)

Com a aplicação em execução, a documentação interativa com todas as rotas e testes via navegador pode ser acessada em:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🗄️ Banco de Dados (Console H2)

O projeto utiliza o banco de dados relacional em memória H2. O console web está habilitado e pode ser acessado durante a execução da aplicação:

- **URL de acesso:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Driver Class:** `org.h2.Driver`
- **JDBC URL:** `jdbc:h2:mem:bibliotecadb`
- **User Name:** `sa`
- **Password:** *(deixe em branco)*

---

## 📡 Endpoints da API

### 👤 Autores (`/autores`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `POST` | `/autores` | Cadastra um novo autor | `201 Created` |
| `GET` | `/autores` | Lista todos os autores | `200 OK` |
| `GET` | `/autores/{id}` | Busca autor pelo ID | `200 OK` |
| `PUT` | `/autores/{id}` | Atualiza nome e e-mail de um autor | `200 OK` |
| `DELETE` | `/autores/{id}` | Exclui um autor | `204 No Content` |

### 📖 Livros (`/livros`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `POST` | `/livros` | Cadastra um livro vinculado a um autor | `201 Created` |
| `GET` | `/livros` | Lista todos os livros cadastrados | `200 OK` |
| `GET` | `/livros/{id}` | Busca livro pelo ID | `200 OK` |
| `PUT` | `/livros/{id}` | Atualiza dados do livro | `200 OK` |
| `DELETE` | `/livros/{id}` | Exclui um livro | `204 No Content` |

### 👥 Usuários (`/usuarios`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `POST` | `/usuarios` | Cadastra um novo usuário | `201 Created` |
| `GET` | `/usuarios` | Lista todos os usuários | `200 OK` |
| `GET` | `/usuarios/{id}` | Busca usuário pelo ID | `200 OK` |
| `PUT` | `/usuarios/{id}` | Atualiza nome e e-mail do usuário | `200 OK` |
| `DELETE` | `/usuarios/{id}` | Exclui um usuário | `204 No Content` |

### 🔄 Empréstimos (`/emprestimos`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `POST` | `/emprestimos` | Registra um novo empréstimo | `201 Created` |
| `GET` | `/emprestimos` | Lista todos os empréstimos | `200 OK` |
| `GET` | `/emprestimos/{id}` | Busca empréstimo pelo ID | `200 OK` |
| `PUT` | `/emprestimos/{id}/devolver` | Registra a devolução do livro | `200 OK` |
| `GET` | `/emprestimos/ativos` | Lista empréstimos em aberto | `200 OK` |
| `GET` | `/emprestimos/atrasados` | Lista empréstimos ativos com data prevista vencida | `200 OK` |
| `GET` | `/emprestimos/usuario/{usuarioId}` | Histórico completo de empréstimos do usuário | `200 OK` |
| `GET` | `/emprestimos/usuario/{usuarioId}/ativos` | Empréstimos ativos de um usuário específico | `200 OK` |

---

## 💡 Exemplos de Requisições e Respostas

### 1. Cadastrar Autor
**POST** `/autores`
```json
{
  "nome": "Machado de Assis",
  "email": "machado@email.com"
}
```
**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "Machado de Assis",
  "email": "machado@email.com"
}
```

---

### 2. Cadastrar Livro
**POST** `/livros`
```json
{
  "titulo": "Dom Casmurro",
  "isbn": "978-85-359-0277-5",
  "anoPublicacao": 1899,
  "autorId": 1
}
```
**Resposta (201 Created):**
```json
{
  "id": 1,
  "titulo": "Dom Casmurro",
  "isbn": "978-85-359-0277-5",
  "anoPublicacao": 1899,
  "autorId": 1,
  "autorNome": "Machado de Assis"
}
```

---

### 3. Cadastrar Usuário
**POST** `/usuarios`
```json
{
  "nome": "Carlos Silva",
  "email": "carlos.silva@email.com"
}
```
**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "Carlos Silva",
  "email": "carlos.silva@email.com"
}
```

---

### 4. Realizar Empréstimo
**POST** `/emprestimos`
```json
{
  "usuarioId": 1,
  "livroId": 1
}
```
**Resposta (201 Created):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "usuarioNome": "Carlos Silva",
  "livroId": 1,
  "livroTitulo": "Dom Casmurro",
  "dataEmprestimo": "2026-10-22",
  "dataPrevistaDevolucao": "2026-10-29",
  "dataDevolucao": null
}
```

---

### 5. Devolver Livro
**PUT** `/emprestimos/1/devolver`

**Resposta (200 OK):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "usuarioNome": "Carlos Silva",
  "livroId": 1,
  "livroTitulo": "Dom Casmurro",
  "dataEmprestimo": "2026-10-22",
  "dataPrevistaDevolucao": "2026-10-29",
  "dataDevolucao": "2026-10-22"
}
```

---

### 6. Exemplo de Resposta de Erro de Validação
**POST** `/livros` com corpo vazio `{}`
```json
{
  "status": 400,
  "erro": "Erro de validação",
  "mensagens": {
    "titulo": "O título é obrigatório",
    "isbn": "O ISBN é obrigatório",
    "anoPublicacao": "O ano de publicação é obrigatório",
    "autorId": "O ID do autor é obrigatório"
  },
  "data": "2026-10-22T12:00:00"
}
```

---

## 🧪 Testes Automatizados

O projeto conta com mais de 40 testes unitários e de contexto garantindo cobertura das regras de negócio, serviços e entidades.

Para rodar todos os testes automatizados via terminal:

- **No Windows:**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

- **No Linux / macOS:**
  ```bash
  ./mvnw test
  ```

### Cobertura de Testes:
- **`AutorServiceTest`:** Testes de criação, validação de e-mail duplicado, busca por ID, atualização e exclusão.
- **`LivroServiceTest`:** Testes de cadastro com autor existente, validação de ISBN único, busca, atualização e exclusão.
- **`UsuarioServiceTest`:** Validações de criação, unicidade de e-mail, buscas e exclusão.
- **`EmprestimoServiceTest`:** Testes de regras de negócio (livro já emprestado, limite máximo de 3 livros, cálculo de devolução em 7 dias, devolução já efetuada, filtros de atrasados e ativos).
- **`BibliotecaApplicationTests`:** Teste de carregamento e inicialização do contexto do Spring Boot.

---

## 📂 Estrutura do Projeto

```
src/main/java/com/biblioteca/biblioteca/
├── BibliotecaApplication.java        # Ponto de entrada da aplicação Spring Boot
├── controller/                       # Controladores REST da API
│   ├── AutorController.java
│   ├── EmprestimoController.java
│   ├── LivroController.java
│   └── UsuarioController.java
├── dto/                              # Objetos de Transferência de Dados (Requests e Responses)
│   ├── AutorRequest.java / AutorResponse.java
│   ├── EmprestimoRequest.java / EmprestimoResponse.java
│   ├── LivroRequest.java / LivroResponse.java
│   └── UsuarioRequest.java / UsuarioResponse.java
├── exception/                        # Classes de exceções customizadas e GlobalExceptionHandler
│   ├── GlobalExceptionHandler.java
│   ├── RecursoJaExisteException.java
│   └── RecursoNaoEncontradoException.java
├── model/                            # Entidades mapeadas para o banco de dados (JPA)
│   ├── Autor.java
│   ├── Emprestimo.java
│   ├── Livro.java
│   └── Usuario.java
├── repository/                       # Interfaces Spring Data JPA para acesso ao banco
│   ├── AutorRepository.java
│   ├── EmprestimoRepository.java
│   ├── LivroRepository.java
│   └── UsuarioRepository.java
└── service/                          # Camada de lógica de negócio e regras do sistema
    ├── AutorService.java
    ├── EmprestimoService.java
    ├── LivroService.java
    └── UsuarioService.java
```

---

## 🎯 Objetivo do Projeto

Projeto prático desenvolvido para consolidar e demonstrar conhecimentos em:
- Arquitetura de APIs RESTful com **Spring Boot**.
- Modelagem de dados e relacionamentos com **Spring Data JPA / Hibernate**.
- Validação declarativa com **Bean Validation**.
- Tratamento centralizado de erros e respostas padronizadas via `@RestControllerAdvice`.
- Documentação viva de APIs com **OpenAPI / Swagger**.
- Testes unitários com mocks utilizando **JUnit 5** e **Mockito**.
