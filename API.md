# Documentação dos Endpoints da API

## API de Endereços (`/api/addresses`)

- **POST** `/api/addresses` - Criar um novo endereço
- **GET** `/api/addresses` - Obter todos os endereços
- **GET** `/api/addresses/{id}` - Obter endereço por ID
- **PUT** `/api/addresses/{id}` - Atualizar endereço por ID
- **DELETE** `/api/addresses/{id}` - Deletar endereço por ID
- **GET** `/api/addresses/check` - Endpoint de verificação de saúde

## API de Clientes (`/api/clients`)

- **POST** `/api/clients` - Criar um novo cliente
- **GET** `/api/clients` - Obter todos os clientes
- **GET** `/api/clients/{id}` - Obter cliente por ID
- **PUT** `/api/clients/{id}` - Atualizar cliente por ID
- **DELETE** `/api/clients/{id}` - Deletar cliente por ID
- **GET** `/api/clients/check` - Endpoint de verificação de saúde

## API de Contas Contratuais (`/api/contract-accounts`)

- **POST** `/api/contract-accounts` - Criar uma nova conta contratual
- **GET** `/api/contract-accounts` - Obter todas as contas contratuais
- **GET** `/api/contract-accounts/minimal` - Obter todas as contas contratuais sem detalhes de instalação
- **GET** `/api/contract-accounts/{id}` - Obter conta contratual por ID
- **PUT** `/api/contract-accounts/{id}` - Atualizar conta contratual por ID
- **DELETE** `/api/contract-accounts/{id}` - Deletar conta contratual por ID
- **GET** `/api/contract-accounts/check` - Endpoint de verificação de saúde

## API de Instalações (`/api/installations`)

- **POST** `/api/installations` - Criar uma nova instalação
- **GET** `/api/installations` - Obter todas as instalações
- **GET** `/api/installations/minimal` - Obter todas as instalações sem detalhes de endereço
- **GET** `/api/installations/{id}` - Obter instalação por ID
- **PUT** `/api/installations/{id}` - Atualizar instalação por ID
- **DELETE** `/api/installations/{id}` - Deletar instalação por ID
- **GET** `/api/installations/check` - Endpoint de verificação de saúde

## API de Ordens de Serviço (`/api/service-orders`)

### Operações CRUD
- **POST** `/api/service-orders` - Criar uma nova ordem de serviço
- **GET** `/api/service-orders` - Obter todas as ordens de serviço
- **GET** `/api/service-orders/{id}` - Obter ordem de serviço por ID
- **PUT** `/api/service-orders/{id}` - Atualizar ordem de serviço por ID
- **DELETE** `/api/service-orders/{id}` - Deletar ordem de serviço por ID

### Operações de Filtro
- **GET** `/api/service-orders/status/{status}` - Obter ordens de serviço por status
- **GET** `/api/service-orders/target/{targetId}` - Obter ordens de serviço por ID do alvo
- **GET** `/api/service-orders/target/distance?min={min}&max={max}` - Obter ordens de serviço por faixa de distância do alvo
- **GET** `/api/service-orders/target/signature/{signature}` - Obter ordens de serviço por assinatura do alvo
- **GET** `/api/service-orders/target/signature/contains/{partial}` - Obter ordens de serviço por assinatura parcial do alvo
- **GET** `/api/service-orders/older-than/{days}` - Obter ordens de serviço mais antigas que os dias especificados
- **GET** `/api/service-orders/created-between?start={start}&end={end}` - Obter ordens de serviço criadas entre datas

### Verificação de Saúde
- **GET** `/api/service-orders/check` - Endpoint de verificação de saúde

## API de Alvos (`/api/targets`)

### Operações CRUD
- **POST** `/api/targets` - Criar um novo alvo
- **GET** `/api/targets` - Obter todos os alvos
- **GET** `/api/targets/{id}` - Obter alvo por ID
- **PUT** `/api/targets/{id}` - Atualizar alvo por ID
- **DELETE** `/api/targets/{id}` - Deletar alvo por ID

### Operações de Filtro por Relacionamento
- **GET** `/api/targets/contract/{contractAccountId}` - Obter alvos por ID da conta contratual
- **GET** `/api/targets/client/{clientId}` - Obter alvos por ID do cliente
- **GET** `/api/targets/type/{type}` - Obter alvos por tipo

### Operações de Filtro por Assinatura
- **GET** `/api/targets/signature/{signature}` - Obter alvos por assinatura exata
- **GET** `/api/targets/signature/contains/{partial}` - Obter alvos por assinatura parcial

### Operações de Filtro por Pontuação
- **GET** `/api/targets/score/greater/{value}` - Obter alvos com pontuação maior que o valor
- **GET** `/api/targets/score/less/{value}` - Obter alvos com pontuação menor que o valor
- **GET** `/api/targets/score/between?min={min}&max={max}` - Obter alvos com pontuação entre min e max

### Operações de Filtro por Distância
- **GET** `/api/targets/distance/greater/{min}` - Obter alvos com distância maior que min
- **GET** `/api/targets/distance/less/{max}` - Obter alvos com distância menor que max
- **GET** `/api/targets/distance/between?min={min}&max={max}` - Obter alvos com distância entre min e max

### Verificação de Saúde
- **GET** `/api/targets/check` - Endpoint de verificação de saúde