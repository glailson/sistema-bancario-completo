# 🏦 Banco Leoncio - Sistema Bancário Fullstack

Este é um sistema bancário completo desenvolvido para gerenciar contas, clientes e transações financeiras. O projeto foca em integridade de dados, tratando cenários de **concorrência** com bloqueio pessimista e oferecendo uma interface moderna em Angular.

## 🚀 Tecnologias Utilizadas

### Backend (Java)
- **Spring Boot 3.x**
- **Spring Data JPA** (Persistência)
- **H2 Database** (Banco de dados em memória para testes)
- **Lombok** (Produtividade)
- **Validação de Dados**: Bean Validation (@PositiveOrZero).
- **Concorrência**: Implementado `Pessimistic Locking` para garantir a consistência do saldo em transações simultâneas.

### Frontend (Angular)
- **Angular 17+** (Standalone Components)
- **Bootstrap 5** & **Bootstrap Icons** (Design)
- **RxJS** (Tratamento de Observables e Erros)

---

## 🛠️ Funcionalidades

- **Cadastro de Clientes**: Criação de novas contas com saldo inicial validado.
- **Área do Cliente**: Visualização de saldo e extrato detalhado.
- **Transferências Seguras**: Sistema de transferência entre contas com rollback automático em caso de erro.
- **Extrato Dinâmico**: Identificação visual de entradas (verde) e saídas (vermelho).

---

## 📋 Como Rodar o Projeto

### 1. Pré-requisitos
- Java 21 ou superior.
- Node.js e Angular CLI instalados.
- IDE de sua preferência (IntelliJ recomendado).

### 2. Rodando o Backend (Java)
1. Navegue até a pasta raiz do projeto.
2. Execute o comando:
   ```bash
   ./mvnw spring-boot:run
