# Fornello API

Uma API RESTful para gerenciamento de pedidos de uma pizzaria/restaurante, desenvolvida usando Spring Boot.

## 📋 Sobre o projeto

Fornello API é um sistema de gerenciamento para pizzaria que permite:
- Cadastro e autenticação de usuários
- Gerenciamento de produtos e variações de produtos
- Processamento de pedidos
- Gerenciamento de entregas
- Controle de status dos pedidos

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- Spring Security
- JWT para autenticação
- H2 Database (em memória)
- Lombok
- MapStruct
- Maven

## 📦 Estrutura do Projeto

```
fornello-api/
├── src/
│   ├── main/
│   │   ├── java/pedroleonez/fornello/api/
│   │   │   ├── config/             # Configurações do Spring
│   │   │   ├── controllers/        # Controladores REST
│   │   │   ├── cors/               # Configuração de CORS
│   │   │   ├── dtos/               # Objetos de Transferência de Dados
│   │   │   │   ├── input/          # DTOs para entrada de dados
│   │   │   │   └── output/         # DTOs para saída de dados
│   │   │   ├── entities/           # Entidades JPA
│   │   │   ├── enums/              # Enumeradores
│   │   │   ├── exceptions/         # Classes de exceção
│   │   │   ├── mappers/            # Mapeadores (MapStruct)
│   │   │   ├── repositories/       # Repositórios JPA
│   │   │   ├── security/           # Configurações de segurança
│   │   │   └── services/           # Serviços da aplicação
│   │   └── resources/
│   │       ├── application.properties # Configurações da aplicação
│   │       ├── messages.properties    # Mensagens de internacionalização
│   │       └── db/migration/          # Scripts de migração
│   └── test/                          # Testes da aplicação
└── pom.xml                            # Configurações do Maven
```

## 🚀 Executando o projeto

### Pré-requisitos

- JDK 17
- Maven

### Passos para execução

1. Clone o repositório
```bash
git clone https://github.com/pedroleonez/fornello-api.git
cd fornello-api
```

2. Compile o projeto
```bash
mvn clean install
```

3. Execute a aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## 📖 Documentação da API

### Autenticação
A API utiliza autenticação baseada em JWT (JSON Web Token). Para acessar endpoints protegidos, é necessário incluir o token no cabeçalho de autorização:
```
Authorization: Bearer {seu-token}
```

### Principais Endpoints

#### Produtos
- `POST /api/products` - Criar um novo produto
- `GET /api/products` - Listar todos os produtos
- `GET /api/products/{productId}` - Obter um produto específico
- `PATCH /api/products/{productId}` - Atualizar parcialmente um produto
- `DELETE /api/products/{productId}` - Excluir um produto
- `POST /api/products/{productId}/variation` - Adicionar uma variação de produto
- `PUT /api/products/{productId}/variation/{productVariationId}` - Atualizar uma variação de produto
- `DELETE /api/products/{productId}/variation/{productVariationId}` - Excluir uma variação de produto

#### Pedidos
- `POST /api/orders` - Criar um novo pedido
- `GET /api/orders` - Listar todos os pedidos (paginados)
- `GET /api/orders/{orderId}` - Obter um pedido específico
- `GET /api/orders/status/{statusName}` - Listar pedidos por status (paginados)
- `PATCH /api/orders/{orderId}/status` - Atualizar status de um pedido
- `DELETE /api/orders/{orderId}` - Excluir um pedido

#### Usuários
- Endpoints para registro e autenticação de usuários

## 💼 Regras de Negócio

- Usuários podem ter diferentes papéis (CUSTOMER, ADMIN)
- Clientes só podem acessar seus próprios pedidos
- Administradores podem acessar e gerenciar todos os pedidos
- Pedidos podem ter diferentes status (PENDENTE, EM_PREPARO, SAIU_PARA_ENTREGA, ENTREGUE, CANCELADO)
- Produtos podem ter múltiplas variações (ex: tamanhos diferentes de pizza)

## 🗃️ Banco de Dados

A aplicação utiliza o banco de dados H2 em memória para desenvolvimento e testes.
Console H2 disponível em: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuário: sa
- Senha: (vazio)

## 🔒 Segurança

- Autenticação baseada em JWT
- Proteção contra CSRF
- Configuração de CORS

## 👥 Contribuição

Para contribuir com este projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature
3. Envie suas alterações com commits descritivos
4. Faça um pull request

## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

Desenvolvido por Pedro Leonez