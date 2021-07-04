# ApiPaint
Service Paint

tópicos:

* Principais frameworks para testes unitários em Java: JUnit, Mockito e Hamcrest. 
* Desenvolvimento de testes unitários para validação de funcionalides básicas: criação, listagem, consulta por nome e exclusão de tintas.
* TDD: apresentação e exemplo prático em 2 funcionaliades importantes: incremento e decremento do número de tintas no estoque.

Para executar o projeto no terminal, digite o seguinte comando:

```shell script
mvn spring-boot:run 
```

Para executar a suíte de testes desenvolvida durante a live coding, basta executar o seguinte comando:

```shell script
mvn clean test
```

Após executar o comando acima, basta apenas abrir o seguinte endereço e visualizar a execução do projeto:

```
http://localhost:8080/api/v1/paints
```

São necessários os seguintes pré-requisitos para a execução do projeto:

* Java 11 ou versões superiores.
* Maven 3.6.3 ou versões superiores.
* Controle de versão GIT instalado.


