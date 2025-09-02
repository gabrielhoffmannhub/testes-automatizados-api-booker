# Teste Automatizado da API Restful Booker

## Sobre o projeto

Este projeto contém testes automatizados para a API pública do site [Restful Booker](https://restful-booker.herokuapp.com/), que simula um sistema de reservas de hotéis.

## Tecnologias usadas

- **Java**: linguagem usada para escrever os testes.  
- **RestAssured**: biblioteca para facilitar o teste de APIs REST.  
- **JUnit 5**: framework para organizar e executar os testes.  
- **Faker**: para gerar dados aleatórios nos testes.  
- **Maven**: ferramenta para gerenciar dependências e executar o projeto.

## Pré-requisitos para rodar os testes

Para rodar os testes no seu computador, você precisa ter instalado:

- **Java JDK 11 ou superior**  
  [Como instalar Java](https://www.oracle.com/java/technologies/javase-downloads.html)

- **Maven**  
  [Como instalar Maven](https://maven.apache.org/install.html)

- **Internet ativa** (para acessar a API durante os testes)

- **IDE (opcional)**, como IntelliJ IDEA ou Eclipse, para abrir e editar o projeto.

## Como rodar os testes

1. Clone o repositório no seu computador:  
   ```bash
   git clone https://github.com/gabrielhoffmannhub/testes-automatizados-api-booker.git

2. Navegue até a pasta do projeto:
   ```bash
   cd testes-automatizados-api-booker

3. Execute os testes usando Maven:
   ```bash
   mvn test
4. Aguarde a execução dos testes no terminal. Você verá se passaram ou falharam.

## O que está sendo testado
- Se a API está ativa (ping)

- Autenticação com usuário e senha

- Criação, atualização e exclusão de reservas

- Comportamento da API em casos de erro (credenciais inválidas, dados incompletos, etc)
