# pw-barbearia

Sistema web de barbearia desenvolvido com Quarkus.

## Estrutura

```text
src/main/java/ifg/edu/br/
  controller/          Rotas HTTP e telas Qute
  model/entity/        Entidades JPA do dominio

src/main/resources/
  templates/           Paginas Qute
  META-INF/resources/  CSS e JavaScript servidos pelo Quarkus
  application.properties
```

## Rotas principais

- `/` abre a tela de login.
- `/agendamentos` abre o painel de agendamentos.

## Banco de dados

O projeto está configurado para usar PostgreSQL local:

```properties
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/barbearia
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
```

Crie o banco `barbearia` no PostgreSQL antes de iniciar a aplicação.

## Rodando em modo desenvolvimento

```shell
./mvnw quarkus:dev
```

No Windows:

```shell
mvnw.cmd quarkus:dev
```
