# CadastroDeNinjas
## 🚀 Tecnologias

- Java 21
- Spring Boot 3.5.8
- PostgreSQL 17
- Redis (Cache)
- Flyway (Migrations)
- Docker + Docker Compose
- Lombok


## 🛢Banco de Dados(Postgres)
O projeto utiliza **Postgres** como o banco de dados relacional principal:

- Armazena dados persistentes de **ninjas** e **missões**
- Relacionamento **Many-to-One** entre ninjas e missões
- Gerenciado via **Flyway** para versionamento  de schema

### 🔄 Migrations (Flyway):
- Versionamento automático de schema
- Scripts em `src/main/resources/db/migration/`
- Execução automática na inicialização da aplicação
- `spring.jpa.hibernate.ddl-auto=validate` (apenas valida, não altera)



### 📋 Estrutura de tabelas:
```sql
-- Tabela de Ninjas
ninjas (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  idade INTEGER,
  rank VARCHAR(50)
)

-- Tabela de Missões
missoes (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(200) NOT NULL,
  dificuldade VARCHAR(50),
  rank VARCHAR(50)
)

-- Tabela de relacionamento
ninja_missao (
  ALTER TABLE tb_cadastro
  ADD CONSTRAINT fk_missoes
  FOREIGN KEY(missoes_id)
  REFERENCES tb_missoes(id);
)
```
### ⚙️ Configuração (Docker):
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

O projeto utiliza Redis para cache de dados:

- **Cache individual:** 10 minutos (busca por ID)
- **Cache de lista:** 5 minutos (lista completa)
- **Invalidação automática:** ao criar/atualizar/deletar

### Configuração:
```yaml
redis:
  image: redis:alpine
  ports:
    - "6379:6379"
```

### Estratégia de cache:
- `GET /ninjas/{id}` → Cache individual
- `GET /ninjas` → Cache de lista
- `POST /ninjas` → Invalida lista
- `PUT /ninjas/{id}` → Atualiza cache individual + invalida lista
- `DELETE /ninjas/{id}` → Remove cache individual + invalida lista

## 🐳 Docker

O projeto é totalmente **containerizado** com Docker Compose, facilitando o setup e deploy.

### 📦 Containers:
```
┌─────────────────────────────────────────┐
│  cadastro-ninjas-app  (Spring Boot)     │ :8081
├─────────────────────────────────────────┤
│  postgres-ninjas      (PostgreSQL 18)   │ :5432
├─────────────────────────────────────────┤
│  redis-ninjas         (Redis Alpine)    │ :6379
└─────────────────────────────────────────┘
```

### 🚀 Como rodar:

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

**3. Suba os containers:**
```bash
docker compose up -d --build
```

**4. Acesse a aplicação:**
- **API:** http://localhost:8081
- **Documentação:** http://localhost:8081/swagger-ui.html

### 🛠️ Comandos úteis:
```bash
# Ver logs
docker compose logs -f app

# Parar containers
docker compose down

# Parar e limpar dados
docker compose down -v

# Rebuild forçado
docker compose up -d --build --force-recreate
```

### 📁 Estrutura Docker:
```
.
├── Dockerfile              # Multi-stage build
├── docker-compose.yml      # Orquestração dos serviços
├── .env                    # Variáveis de ambiente (não versionado)
├── .env.example            # Template de variáveis
└── .dockerignore          # Arquivos ignorados no build
```

### ⚙️ Dockerfile (Multi-stage):
```dockerfile
# ETAPA 1: Build (Maven + JDK)
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ src/
RUN mvn package -DskipTests

# ETAPA 2: Runtime (apenas JRE)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

**Vantagens:**
- ✅ Imagem final ~250MB (vs ~800MB sem multi-stage)
- ✅ Cache de dependências otimizado
- ✅ Build rápido (30s após primeira vez)

---

## 🧪 Testes

### Ambiente de teste:
- **Banco de dados:** H2 (em memória)
- **Configuração:** `src/test/resources/application.properties`

### Rodar testes:
```bash
# Via Maven
mvn test

# Via Docker (durante build)
docker compose up --build
```

---

## 📚 Estrutura do Projeto
```
src/
├── main/
│   ├── java/
│   │   └── dev/nadsonaguiar/CadastroDeNinjas/
│   │       ├── Ninjas/
│   │       │   ├── NinjaController.java
│   │       │   ├── NinjaService.java
│   │       │   ├── NinjaRepository.java
│   │       │   ├── NinjaModel.java
│   │       │   ├── NinjaDTO.java
│   │       │   └── NinjaMapper.java
│   │       ├── Missoes/
│   │       │   └── ...
│   │       └── Config/
│   │           └── RedisConfig.java
│   └── resources/
│       ├── db/migration/          # Scripts Flyway
│       ├── static/                # Frontend HTML
│       └── application.properties
└── test/
    ├── java/
    └── resources/
        └── application.properties # Config de teste (H2)
```

---


### Próximos passos

🔐 PRÓXIMA ETAPA: Spring Security + JWT (3-4 dias)

O que vamos fazer:

1. Adicionar dependências (5min)
2. Criar entidade User (15min)
3. Implementar JWT (1-2h)
4. Configurar Security (1-2h)
5. Proteger endpoints (30min)
6. Testar (30min)

Endpoints que vamos criar:

POST /auth/register  → Criar usuário
POST /auth/login     → Retorna JWT token
GET  /ninjas         → Público (todos podem ver)
POST /ninjas         → Protegido (só ADMIN)
PUT  /ninjas/{id}    → Protegido (só ADMIN)
DELETE /ninjas/{id}  → Protegido (só ADMIN)

Roles:
- 'USER' → pode ver ninjas
- 'ADMIN' → pode criar/editar/deletar
