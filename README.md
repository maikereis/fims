# Field Inspection Management System

![Java](https://img.shields.io/badge/Java-21-007396?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.5.6-C71A36?logo=apachemaven&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Coverage Badge](https://github.com/maikereis/fims/blob/gh-pages/coverage.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
---

## Sobre

O FIMS (Field Inspection Management System) é um sistema desenvolvido em Java com Spring Boot, projetado para centralizar e gerenciar alvos de inspeção de campo.
Seu principal objetivo é fornecer uma plataforma unificada para o controle completo do ciclo de inspeção — desde a identificação e priorização de alvos até o acompanhamento das ordens de serviço e inspeções executadas.

Destina-se a empresas que precisam gerir operações de campo de forma eficiente, auditável e orientada a dados, garantindo maior controle e transparência sobre suas atividades externas.

## Motivação

Em muitas operações de campo — como serviços públicos, auditorias de infraestrutura, inspeções de segurança ou monitoramento ambiental — as equipes frequentemente têm dificuldades para coordenar inspeções e manter a rastreabilidade entre metas, tarefas e inspeções executadas.

O FIMS resolve esse problema oferecendo um serviço de back-end minimalista, porém robusto, para:

* Registrar e monitorar alvos de inspeção originados de diferentes fontes (machine learning, regras de negócio, denúncias, etc.);

* Controlar quais inspeções foram geradas a partir de quais alvos, mantendo rastreabilidade total;

* Acompanhar o status das ordens de serviço e o progresso das inspeções em campo;

* Obter uma visão estruturada e integrada das operações externas, facilitando a tomada de decisão e o planejamento estratégico.

## Features

* CRUD APIs
* Camadas de service e repository com Spring Boot
* Persistência em H2 para desenvolvimento rápido
* Build com Maven (pom.xml)
* Coleção Postman (postman.json) para testar os endpoints
* Código modular, permitindo extensões como autenticação, dashboards e integração com ML

## Documentação

- [Endpoints da API](API.md) - Documentação completa de todos os endpoints

## Requisitos

| Dependência    | Versão mínima |
| -------------- | ------------- |
| Java           | 21            |
| Maven          | 3.5.6+        |
| Banco de dados | H2 (default)  |


## Relacionamentos entre classes

**Address → Installation**

```
Address "1" --> "*" Installation : has (CASCADE.ALL)
```
* Significado: Um Address (endereço) pode estar associado a múltiplas Installations (instalações).
Exemplo: um prédio (um endereço) pode conter várias instalações de serviço, como diferentes medidores de energia ou unidades operacionais.

* CASCADE.ALL: Caso um Address seja removido ou persistido, todas as suas Installations associadas serão automaticamente atualizadas ou excluídas.

**Installation → ContractAccount**

```
Installation "1" --> "*" ContractAccount : has (orphanRemoval)
```

* Significado: Uma Installation pode estar vinculada a várias ContractAccounts (contas contratuais).
Exemplo: uma mesma instalação pode ter tido diferentes contratos ativos ao longo do tempo.

* orphanRemoval: Se uma ContractAccount for removida da lista de uma Installation, ela será automaticamente excluída do banco de dados.

**Client → ContractAccount**

```
Client "1" --> "*" ContractAccount : owns (CASCADE.ALL)
```

* Significado: Um Client (cliente, pessoa ou empresa) pode possuir múltiplas ContractAccounts.
Exemplo: um cliente pode manter diversos contratos de energia em locais diferentes.

* CASCADE.ALL: Quando um Client é modificado, as ContractAccounts associadas também são afetadas (criadas, atualizadas ou removidas automaticamente).

**ContractAccount → Client**

```
ContractAccount "*" --> "1" Client : belongs to
```

* Significado: Cada ContractAccount pertence a exatamente um Client (relação inversa da anterior).

**ContractAccount → Installation**

```
ContractAccount "*" --> "1" Installation : belongs to
```

* Significado: Cada ContractAccount está vinculada a uma única Installation, que representa o local físico do serviço.
Assim, a combinação de Client + Installation define de forma única um contrato.

**ContractAccount → Target**

```
ContractAccount "1" --> "*" Target : has
```

* Significado: Uma ContractAccount pode ter múltiplos Targets.
Os Targets representam análises, metas ou avaliações associadas ao contrato — por exemplo, detecção de fraude, pontuação de machine learning ou priorização de serviços.

**Target → ContractAccount**

```
Target "*" --> "1" ContractAccount : belongs to
```

* Significado: Cada Target está associado a exatamente uma ContractAccount (relação inversa da anterior).

**Target → ServiceOrder**

```
Target "1" --> "*" ServiceOrder : has (CASCADE.ALL)
```

* Significado: Um Target (por exemplo, uma conta sinalizada por regras ou modelos de ML) pode gerar várias ServiceOrders (ordens de serviço, inspeções ou intervenções de campo).

* CASCADE.ALL: As ServiceOrders são gerenciadas em conjunto com o Target — ou seja, são persistidas ou removidas automaticamente junto a ele.

**ServiceOrder → Target**

```
ServiceOrder "*" --> "1" Target : belongs to
```

* Significado: Cada ServiceOrder está vinculada a exatamente um Target, representando a origem ou motivação daquela ordem de serviço.

## Diagrama de Relacionamentos

<img src="img/relationships.png" weight=2000px width=2000px>