# 📚 [DESENVOLVIMENTO WEB] Carteira Facil - API REST

## 📝 Visão Geral do Projeto

Este projeto foi desenvolvido como parte dos requisitos da disciplina de **Desenvolvimento Web** e tem como objetivo principal criar uma aplicação de **gestão de finanças pessoais simples**. A aplicação consiste em uma API RESTful completa que gerencia as operações básicas de uma carteira digital (Usuários, Ganhos e Gastos).

### Objetivo Acadêmico

Demonstrar a proficiência na construção de uma arquitetura de software moderna, utilizando o ecossistema Spring Boot, e garantir a persistência e integridade dos dados através de ferramentas de mercado (PostgreSQL e Flyway).

## 🚀 Tecnologias e Arquitetura

| Camada | Tecnologia | Função |
| :--- | :--- | :--- |
| **Backend** | Java 17+ / Spring Boot 3.x | Desenvolvimento da lógica de negócios e exposição dos endpoints REST. |
| **Banco de Dados** | PostgreSQL | SGBD relacional robusto para armazenamento de dados. |
| **Persistência** | Spring Data JPA / Hibernate | Mapeamento Objeto-Relacional (ORM) para o CRUD. |
| **Controle de Schema** | Flyway | Gerencia a migração do banco de dados (criação de tabelas, funções e triggers). |
| **Documentação** | Springdoc OpenAPI (Swagger UI) | Geração automática de documentação interativa para teste e consumo da API. |
| **Integridade** | Triggers PL/pgSQL | Mecanismos de banco de dados para calcular e manter o saldo (`renda_atual`) em tempo real. |

## ⚙️ Pré-requisitos para Execução

Para rodar este projeto, você precisa ter instalado:

1. **Java Development Kit (JDK) 17 ou superior.**
2. **Maven** (Gerenciador de dependências).
3. **Docker** (Essencial para iniciar o ambiente PostgreSQL).

## 💾 Guia de Configuração e Execução

### Passo 1: Iniciar o Banco de Dados PostgreSQL (Docker)

Utilizamos o Docker para garantir um ambiente de banco de dados consistente e rápido.

Execute o comando abaixo no seu terminal para iniciar o container do PostgreSQL, mapeando a porta e criando o banco de dados inicial:

```bash
docker run --name dev-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=root -e POSTGRES_DB=carteira_facil -p 5432:5432 -d postgres:latest
```

### Passo 2: Configurar a Conexão no Spring Boot

Verifique e confirme se o arquivo de configuração da aplicação (src/main/resources/application.properties) corresponde às credenciais do Docker:

```bash
# Configuração do PostgreSQL (Deve corresponder ao Passo 1)
spring.datasource.url=jdbc:postgresql://localhost:5432/carteira_facil
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuração do Flyway: Permite que o Flyway controle o schema
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.jpa.hibernate.ddl-auto=none
```

### Passo 3: Compilar e Rodar a AplicaçãoNavegue até o diretório raiz do projeto e use o Maven para compilar e executar:Compilar:Bashmvn clean install
Executar:
```bash
mvn spring-boot:run
```
A aplicação estará online na porta 8080.

### 📖 Endpoints da API (Documentação Swagger)

Após a aplicação iniciar, acesse o Swagger UI para testar os endpoints de forma interativa:

🔗 Documentação: http://localhost:8080/swagger-ui.html

### Fluxo de Teste Sugerido
POST /api/usuarios: Crie um novo usuário para obter um usuarioId.

POST /api/ganhos: Registre uma receita usando o usuarioId obtido.

POST /api/gastos: Registre uma despesa usando o mesmo usuarioId.

GET /api/renda/usuario/{usuarioId}: Consulte o saldo. O valor exibido é a prova da integridade do sistema, calculado automaticamente pelas triggers do PostgreSQL.
