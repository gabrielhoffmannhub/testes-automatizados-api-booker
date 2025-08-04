# 📘 Testes Automatizados com Rest Assured

Este projeto contém testes automatizados para a API pública [Restful Booker](https://restful-booker.herokuapp.com/), utilizando **Java**, **JUnit 5** e **Rest Assured**. 

---

## 🧰 O que você precisa instalar

Antes de tudo, é necessário ter os seguintes programas instalados:

| Programa       | Link para download                        |
|----------------|--------------------------------------------|
| Java (JDK 11+) | https://adoptium.net/pt-BR/temurin/releases/ |
| Git            | https://git-scm.com/downloads               |
| Maven          | https://maven.apache.org/download.cgi       |
| (Opcional) IDE | IntelliJ IDEA: https://www.jetbrains.com/idea/download |

> 💡 Você pode verificar se está tudo instalado com os comandos:
> `java -version`, `mvn -version`, `git --version`

---

## 🚀 Como rodar os testes passo a passo

### 1. Baixar o projeto do GitHub

Abra o terminal e digite:
```bash
git clone https://github.com/SEU_USUARIO/rest-assured-booker-tests.git
cd rest-assured-booker-tests
```
---

## 💻 Executar os testes com interface 

Se você quiser abrir o projeto em uma interface:

### Usando o IntelliJ IDEA:
1. Clique em **Open** e selecione a pasta do projeto.
2. Espere carregar o Maven automaticamente.
3. Vá até a classe `TestesRestfulBooker.java`.
4. Clique com o botão direito sobre o método desejado e selecione **Run**.

---

### 3. Rodar os testes com Maven (sem abrir IDE)

No terminal, execute:
```bash
mvn test
```

Você verá saídas no console como:
```
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🧪 O que é testado neste projeto

| Tipo de teste                           | Cobertura |
|----------------------------------------|-----------|
| Verificar se API está online           | ✅         |
| Criar, atualizar e deletar reservas    | ✅         |
| Autenticação com e sem sucesso         | ✅         |
| Casos negativos (erros esperados)      | ✅         |

---

## 📦 Tecnologias utilizadas
- Java 11
- JUnit 5
- Rest Assured 5.3
- Maven

---
Este projeto é educacional.
> Autor: [GabrielSH](https://github.com/gabrielhoffmannhub)
