![Version](https://img.shields.io/badge/version-v2.0.0-blue)
![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)

# 🥷 CadastroDeNinjas

Sistema de gerenciamento de ninjas e missões com autenticação JWT, cache Redis e arquitetura containerizada.

---

## 🚀 Tecnologias

- **Java 21** - Linguagem principal
- **Spring Boot 3.5.8** - Framework
- **Spring Security** - Autenticação e autorização
- **JWT (jjwt 0.12.6)** - Tokens de autenticação
- **PostgreSQL 17** - Banco de dados relacional
- **Redis Alpine** - Sistema de cache
- **Flyway** - Migrations e versionamento de schema
- **Docker + Docker Compose** - Containerização
- **Lombok** - Redução de boilerplate
- **H2** - Banco em memória para testes

---

## 📋 Funcionalidades

### 🔐 Autenticação JWT
- Registro e login de usuários
- Tokens JWT com expiração de 24h
- Senhas criptografadas com BCrypt
- Sistema de roles (USER/ADMIN)
- Filtro de autenticação para rotas protegidas

### 👤 Gestão de Usuários
- Criação de contas com diferentes níveis de acesso
- Autenticação stateless via JWT
- CORS configurado para APIs

### 🥷 CRUD de Ninjas
- Listagem completa com cache
- Busca por ID
- Criação, atualização e remoção (apenas ADMIN)
- Relacionamento Many-to-One com missões

### 🎯 Sistema de Missões
- Cadastro de missões com níveis de dificuldade
- Associação de ninjas às missões
- Gestão completa via API REST

### ⚡ Cache Inteligente
- Cache individual (10 minutos)
- Cache de lista (5 minutos)
- Invalidação automática em operações de escrita

---

## 🔒 Controle de Acesso

### Endpoints Públicos
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|---------|
| `POST` | `/auth/register` | Criar usuário | 🌐 Público |
| `POST` | `/auth/login` | Autenticar | 🌐 Público |

### Endpoints Protegidos
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|---------|
| `POST` | `/ninjas` | Criar ninja | 🔒 ADMIN |
| `PUT` | `/ninjas/{id}` | Atualizar ninja | 🔒 ADMIN |
| `DELETE` | `/ninjas/{id}` | Deletar ninja | 🔒 ADMIN |
 `GET` | `/ninjas` | Listar todos | 🔒 ADMIN |
| `GET` | `/ninjas/{id}` | Buscar por ID | 🔒 ADMIN 
### Roles Disponíveis
- **USER** → Visualização de login e register
- **ADMIN** → Acesso completo (CRUD)

---

## 🛢️ Banco de Dados (PostgreSQL)

### Estrutura de Tabelas

```sql
-- Usuários (Autenticação)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ninjas
CREATE TABLE ninjas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    idade INTEGER,
    rank VARCHAR(50)
);

-- Missões
CREATE TABLE missoes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    dificuldade VARCHAR(50),
    rank VARCHAR(50)
);
```

### 🔄 Migrations (Flyway)
- Versionamento automático de schema
- Scripts em `src/main/resources/db/migration/`
- Execução automática na inicialização
- `spring.jpa.hibernate.ddl-auto=validate` (somente validação)

### ⚙️ Configuração Docker

```yaml
postgres:
  image: postgres:17
  container_name: postgres-ninjas
  environment:
    POSTGRES_DB: cadastro_ninjas
    POSTGRES_USER: postgres
    POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
  ports:
    - "5432:5432"
  volumes:
    - postgres_data:/var/lib/postgresql/data
```

---

## ⚡ Cache (Redis)

### Estratégia de Cache
- **Cache individual:** 10 minutos (busca por ID)
- **Cache de lista:** 5 minutos (lista completa)
- **Invalidação automática:** ao criar/atualizar/deletar

### Operações com Cache

| Operação | Cache Aplicado |
|----------|----------------|
| `GET /ninjas/{id}` | Cache individual |
| `GET /ninjas` | Cache de lista |
| `POST /ninjas` | Invalida lista |
| `PUT /ninjas/{id}` | Atualiza individual + invalida lista |
| `DELETE /ninjas/{id}` | Remove individual + invalida lista |

### Configuração Docker

```yaml
redis:
  image: redis:alpine
  container_name: redis-ninjas
  ports:
    - "6379:6379"
```

---

## 🐳 Docker

### 📦 Arquitetura de Containers

```
┌─────────────────────────────────────────┐
│  cadastro-ninjas-app  (Spring Boot)     │ :8081
├─────────────────────────────────────────┤
│  postgres-ninjas      (PostgreSQL 17)   │ :5432
├─────────────────────────────────────────┤
│  redis-ninjas         (Redis Alpine)    │ :6379
└─────────────────────────────────────────┘
```

### 🚀 Como Executar

**1. Clone o repositório:**
```bash
git clone https://github.com/NadsonAguiar/CadastroDeNinjas.git
cd CadastroDeNinjas
```

**2. Configure as variáveis de ambiente:**
```bash
cp .env.example .env
# Edite o .env com suas credenciais
```

**Variáveis necessárias no `.env`:**
```env
POSTGRES_PASSWORD=sua_senha_postgres
```

**3. Suba os containers:**
```bash
docker compose up -d --build
```

**4. Acesse a aplicação:**
- **API:** http://localhost:8081
- **Documentação:** http://localhost:8081/swagger-ui.html

### 🛠️ Comandos Úteis

```bash
# Ver logs da aplicação
docker compose logs -f app

# Parar containers
docker compose down

# Parar e limpar volumes
docker compose down -v

# Rebuild completo
docker compose up -d --build --force-recreate

# Acessar container da aplicação
docker exec -it cadastro-ninjas-app sh

# Acessar PostgreSQL
docker exec -it postgres-ninjas psql -U postgres -d cadastro_ninjas
```

### 📁 Estrutura Docker

```
.
├── Dockerfile              # Multi-stage build
├── docker-compose.yml      # Orquestração dos serviços
├── .env                    # Variáveis de ambiente (não versionado)
├── .env.example            # Template de variáveis
└── .dockerignore          # Arquivos ignorados no build
```

### ⚙️ Dockerfile (Multi-stage)

```dockerfile
# ETAPA 1: Build (Maven + JDK 21)
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ src/
RUN mvn package -DskipTests

# ETAPA 2: Runtime (apenas JRE 21)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

**Vantagens:**
- ✅ Imagem final ~250MB (vs ~800MB sem multi-stage)
- ✅ Cache de dependências Maven otimizado
- ✅ Build rápido (~30s após primeira execução)
- ✅ Apenas JRE em produção (segurança e performance)

---

## 🔐 Guia de Uso - Autenticação

### 1️⃣ Registrar Usuário

```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "senha123",
    "role": "ADMIN"
  }'
```

### 2️⃣ Fazer Login

```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "senha123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3️⃣ Usar Token em Requisições

```bash
curl -X POST http://localhost:8081/ninjas \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Naruto Uzumaki",
    "email": "naruto@konoha.com",
    "idade": 17,
    "rank": "GENIN"
  }'
```

### 🛡️ Segurança Implementada
- ✅ Senhas criptografadas com BCrypt
- ✅ Tokens JWT com expiração (24 horas)
- ✅ Filtro de autenticação automático
- ✅ CORS configurado
- ✅ API stateless (sem sessões)
- ✅ Proteção CSRF desabilitada (REST API)

---

## 📚 Estrutura do Projeto

```
src/
├── main/
│   ├── java/dev/nadsonaguiar/CadastroDeNinjas/
│   │   ├── Security/
│   │   │   ├── JwtService.java                 # Geração/validação JWT
│   │   │   ├── JwtAuthFilter.java              # Filtro de autenticação
│   │   │   ├── SecurityConfig.java             # Config Spring Security
│   │   │   └── CustomUserDetailsService.java   # Carregamento usuários
│   │   ├── User/
│   │   │   ├── UserModel.java                  # Entidade User
│   │   │   ├── UserRepository.java             # Repository JPA
│   │   │   ├── UserService.java                # Lógica de negócio
│   │   │   └── AuthController.java             # Endpoints auth
│   │   ├── Ninjas/
│   │   │   ├── NinjaController.java
│   │   │   ├── NinjaService.java
│   │   │   ├── NinjaRepository.java
│   │   │   ├── NinjaModel.java
│   │   │   ├── NinjaDTO.java
│   │   │   └── NinjaMapper.java
│   │   ├── Missoes/
│   │   │   ├── MissaoController.java
│   │   │   ├── MissaoService.java
│   │   │   ├── MissaoRepository.java
│   │   │   └── MissaoModel.java
│   │   └── Config/
│   │       └── RedisConfig.java
│   └── resources/
│       ├── db/migration/
│       │   ├── V1__create_tables.sql
│       │   ├── V2__add_rank_tb_cadastro.sql
│       │   └── V3__create_users_table.sql
│       ├── static/                             # Frontend HTML
│       └── application.properties
└── test/
    ├── java/
    └── resources/
        └── application.properties              # Config H2
```

---

## 🧪 Testes

### Ambiente de Teste
- **Banco de dados:** H2 (em memória)
- **Configuração:** `src/test/resources/application.properties`
- **Escopo:** Testes unitários e de integração

### Executar Testes

```bash
# Via Maven
mvn test

# Via Docker (executa durante o build)
docker compose up --build

# Com cobertura
mvn test jacoco:report
```
---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## 👨‍💻 Autor

**Nadson Aguiar**  
GitHub: [@NadsonAguiar](https://github.com/NadsonAguiar)

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/NovaFeature`)
3. Commit suas mudanças (`git commit -m 'Add: nova feature'`)
4. Push para a branch (`git push origin feature/NovaFeature`)
5. Abra um Pull Request
