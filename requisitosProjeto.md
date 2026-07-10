# Projeto Prático — Programação para Web
[cite_start]**Instituto Federal de Goiás — Campus Luziânia** [cite: 3]
[cite_start]**Bacharelado em Sistemas de Informação** [cite: 3]
[cite_start]**Professor:** Prof. Dr. Daniel Lucena [cite: 5]

---

## 📋 Descrição Geral
[cite_start]O projeto prático consiste em implementar um software completo para Web. [cite: 8]
* [cite_start]**Front-end:** HTML, CSS e JS. [cite: 8]
* [cite_start]**Back-end:** Ecossistema Java EE. [cite: 8]
* [cite_start]**Modalidade:** O projeto deverá ser desenvolvido **individualmente**. [cite: 9]

---

## 🛠️ Requisitos do Sistema

### Requisitos Funcionais (RF)
[cite_start]Todas as aplicações (sistema web) devem possuir os seguintes requisitos funcionais comuns: [cite: 10]

1. [cite_start]**Autenticar usuário:** O sistema deverá solicitar a autenticação de usuário por meio de e-mail e senha. [cite: 11] [cite_start]Somente usuários autenticados poderão ter acesso à página principal da aplicação. [cite: 12]
2. [cite_start]**Manter usuário:** O sistema deverá permitir o cadastro de usuários que poderão acessar os recursos da aplicação e executar funções conforme seu perfil de usuário. [cite: 13]
3. [cite_start]**Manter perfil de usuário:** O sistema deverá permitir o cadastro de perfil de usuários com configurações específicas de acesso a cada recurso funcional disponível na aplicação ou possuir papéis específicos para diferentes usuários, com relação de recursos funcionais bem definidos. [cite: 14]
4. [cite_start]**Exibir opções de navegação de recursos:** O sistema deverá apresentar um mecanismo de navegação para todas as interfaces de usuário de modo que permita retornar ao passo anterior ou a interface principal da aplicação. [cite: 15] [cite_start]Uma sugestão para implementação desse mecanismo de navegação é a utilização de âncoras (links) disponíveis em um menu da aplicação. [cite: 16]
5. [cite_start]**Dois casos de uso específico do domínio do seu problema:** Você deverá definir pelo menos dois requisitos funcionais para sua aplicação e desenvolvê-los integralmente. [cite: 17, 24] [cite_start]Não serão aceitos como casos de uso cadastros de dados auxiliares ou cadastros triviais (Ex: cadastro de tipo, cadastro de status, cadastro de categoria, etc...). [cite: 24] [cite_start]Caso sua aplicação possua cadastro de usuário a partir da interface de autenticação (login), é importante que defina pelo menos um caso de uso com necessidade de permissões de acesso distintas. [cite: 24] 
   * [cite_start]*Exemplo:* Caso seja um e-commerce, a função compra pode ser utilizada por qualquer usuário autenticado, porém o cadastro de produtos só pode ser acessado por um usuário com perfil administrador. [cite: 25]
6. [cite_start]**Rastreabilidade e Auditoria:** O sistema deve manter TODAS as ações executadas por todos os usuários a fim de permitir auditoria, logo, um registro de log de uso do sistema deverá ser mantido contendo minimamente: a) a ação executada, b) o usuário executor (caso autenticado) e c) data e hora da ação executada. [cite: 26]

### Requisitos Não Funcionais (RNF)
* [cite_start]**Linguagem Java EE:** O sistema deverá obrigatoriamente ser implementado em linguagem Java. [cite: 28] [cite_start]É desejável que seja utilizada a versão 11 ou superior. [cite: 29]
* [cite_start]**Modelo MVC:** O sistema deverá obrigatoriamente utilizar o modelo de desenvolvimento em camadas MVC independente do framework utilizado. [cite: 30, 31]
* [cite_start]**JAX-RS:** O sistema deverá obrigatoriamente utilizar a especificação JAX-RS para a implementação dos endpoints das requisições REST. [cite: 32]
* [cite_start]**Quarkus:** É desejável que o sistema utilize o framework Quarkus. [cite: 33]
* [cite_start]**Utilização dos padrões DAO e Entity:** Para cada entidade do seu sistema, deverá ser criada uma classe na camada de modelo que represente a estrutura de dados da entidade (Entity) e o respectivo objeto de acesso a dados (DAO) para manter e recuperar os dados da respectiva entidade. [cite: 34]
* [cite_start]**Utilização do padrão BO:** TODAS as regras de negócios deverão ser implementadas utilizando objetos Business Object (BO). [cite: 35] [cite_start]Isso inclui também as validações de dados a serem persistidos. [cite: 36]
* [cite_start]**Comunicação entre back-end e front-end exclusivamente por DTO:** Por motivos de segurança, uma entidade nunca deve ser transitada na estrutura em que é persistida para o front-end. [cite: 43] [cite_start]Logo, para cada interface de usuário (UI), os DTOs necessários para o funcionamento da respectiva UI deverão ser projetados para trafegar somente os dados necessários. [cite: 44]

---

## 📊 Critérios de Avaliação e Notas

[cite_start]A avaliação do projeto prático será realizada subdividida em três avaliações: [cite: 45]

| Critério / Componente | Pontuação Máxima | Descrição / Detalhes |
| :--- | :---: | :--- |
| **Front-End (FE)** | 2.0 pontos | [cite_start]Implementação das tecnologias HTML, CSS e JS. [cite: 46] |
| **Back-end (BE)** | 2.0 pontos | [cite_start]Implementação do ecossistema Java EE. [cite: 47] |
| **Aplicação dos fundamentos (AF)** | 2.0 pontos | [cite_start]Uso correto de MVC, JAX-RS, DAO, Entity, BO e DTO. [cite: 48] |
| **Fluxo completo dos casos de uso (FC)** | 4.0 pontos | [cite_start]Comunicação entre front-end, back-end e persistência. [cite: 49, 50] |
| **Apresentação e Arguição (AA)** | 10.0 pontos | [cite_start]Apresentação coletiva, com pelo menos 3 perguntas por aluno com resposta individual. [cite: 51, 52, 53] |

### Critérios Gerais:
* [cite_start]Corretude [cite: 55]
* [cite_start]Completude [cite: 56]
* [cite_start]Clareza [cite: 57]

### Cálculo da Nota Final Individual:
[cite_start]A nota final individual é obtida através da fórmula: [cite: 58]

$$NF = (FE + BE + AF + FC) * (AA / 10)$$

> [cite_start]🎓 **Aprovação:** O aluno estará aprovado se $NF >= 6.0$. [cite: 60]
> 
> [cite_start]⚠️ **Atenção:** A ausência na avaliação de Apresentação e Arguição implicará na reprovação automática ($AA = 0$). [cite: 73, 74]

---

## 📅 Datas e Regras de Entrega

* [cite_start]**Prazo Limite:** A data e horário limite para TÉRMINO da entrega e apresentação será o penúltimo dia de aula às 22h. [cite: 62]
* [cite_start]**Atrasos:** Não serão aceitos trabalhos após essa data e horário limite. [cite: 63]
* [cite_start]**Apresentações:** Não serão aceitas apresentações após essa data e horário limite. [cite: 71]
* [cite_start]**Segunda Chance:** Não haverá possibilidade de reapresentação. [cite: 72]
* [cite_start]**Repositório:** Os códigos fontes do trabalho deverão ser entregues via repositório de código (Github, Gitlab, etc...). [cite: 75]

---
[cite_start]*Bom trabalho!* [cite: 76]