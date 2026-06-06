# Análise de Código Fonte e Teste de Caixa Branca

## 1. Introdução

O objetivo desta atividade foi aplicar técnicas de teste estrutural (caixa branca) em um código Java responsável pela autenticação de usuários em um banco de dados MySQL.

A análise consistiu na revisão do código-fonte fornecido, identificação de falhas, vulnerabilidades e problemas de organização, construção do grafo de fluxo do programa, cálculo da complexidade ciclomática, identificação dos caminhos básicos independentes e implementação de melhorias seguindo boas práticas de desenvolvimento de software.

O código analisado possui uma classe denominada `User`, responsável por estabelecer conexão com o banco de dados e verificar a existência de um usuário por meio de login e senha.

---

# 2. Análise Estática do Código

## Documentação

O código original não possuía comentários ou documentação explicando o funcionamento dos métodos e variáveis, dificultando a manutenção e compreensão do sistema.

### Problemas Identificados

- Ausência de comentários explicativos;
- Falta de documentação dos métodos.

### Correções Realizadas

- Inserção de comentários explicativos nos principais trechos do código;
- Documentação das correções implementadas.

---

## Nomenclatura

A nomenclatura utilizada apresentava uma padronização aceitável, porém algumas variáveis possuíam escopo inadequado.

### Problemas Identificados

```java
public String nome = "";
public boolean result = false;
```

As variáveis eram públicas e utilizadas apenas internamente pela classe.

### Correções Realizadas

- Remoção das variáveis globais desnecessárias;
- Utilização de variáveis locais dentro dos métodos.

---

## Legibilidade

O código original construía consultas SQL utilizando concatenação de strings.

### Problemas Identificados

```java
sql += "where login = '" + login + "'";
sql += " and senha = '" + senha + "'";
```

Essa prática reduz a legibilidade e aumenta riscos de segurança.

### Correções Realizadas

- Substituição da concatenação por `PreparedStatement`;
- Simplificação da construção da consulta SQL.

---

## Tratamento de Exceções

### Problemas Identificados

```java
catch(Exception e){}
```

As exceções eram ignoradas, dificultando a identificação de erros.

### Correções Realizadas

```java
catch(Exception e){
    e.printStackTrace();
}
```

Agora as exceções são registradas, facilitando a manutenção e depuração.

---

## Segurança

Foram identificadas vulnerabilidades relevantes relacionadas ao acesso ao banco de dados.

### SQL Injection

A consulta SQL era construída diretamente a partir dos dados informados pelo usuário, permitindo manipulação da consulta.

### Credenciais Expostas

As credenciais do banco estavam armazenadas diretamente no código.

```java
jdbc:mysql://127.0.0.1/test?user=lopes&password=123
```

### Senhas em Texto Puro

O sistema realizava comparação direta da senha informada com a senha armazenada no banco.

---

## Conexões

### Problemas Identificados

Os objetos de conexão não eram fechados corretamente após o uso:

- Connection
- Statement
- ResultSet

### Correções Realizadas

Implementação de `try-with-resources`, garantindo o fechamento automático dos recursos.

---

## Vulnerabilidades Encontradas

| Vulnerabilidade | Severidade |
|----------------|------------|
| SQL Injection | Alta |
| Senhas em texto puro | Alta |
| Credenciais expostas | Média |
| Tratamento inadequado de exceções | Média |
| Vazamento de recursos | Média |

---

## Boas Práticas Aplicadas

- Utilização de `PreparedStatement`;
- Tratamento adequado de exceções;
- Fechamento automático de recursos;
- Organização das constantes de conexão;
- Inserção de comentários explicativos;
- Remoção de variáveis globais desnecessárias.

---

# 3. Grafo de Fluxo

## Grafo Desenvolvido



```md
<img width="1077" height="137" alt="image" src="https://github.com/user-attachments/assets/354fd93f-1bf0-4421-b54e-c3314804e047" />
```

## Explicação dos Fluxos

O fluxo inicia com a abertura da conexão com o banco de dados e a execução da consulta SQL.

Após a execução da consulta, existem duas possibilidades:

1. O usuário é encontrado no banco de dados;
2. O usuário não é encontrado.

Além desses fluxos, existe um terceiro caminho correspondente ao tratamento de exceções, executado quando ocorre algum erro durante a conexão ou execução da consulta.

---

# 4. Complexidade Ciclomática

A complexidade ciclomática foi calculada utilizando a fórmula:

### Fórmula

V(G) = E − N + 2P

Onde:

- E = Número de arestas
- N = Número de nós
- P = Número de componentes conectados

### Quantidade de Nós

```text
N = 11
```

### Quantidade de Arestas

```text
E = 12
```

### Quantidade de Componentes Conectados

```text
P = 1
```

### Cálculo

```text
V(G) = E - N + 2P

V(G) = 12 - 11 + 2(1)

V(G) = 12 - 11 + 2

V(G) = 3
```

### Resultado Final

```text
Complexidade Ciclomática = 3
```

O método analisado possui três caminhos independentes de execução.

---

# 5. Caminhos Básicos

## Caminho 1 – Usuário Encontrado

### Fluxo

```text
1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 10 → 11
```

### Descrição

- Conexão realizada com sucesso;
- Consulta executada corretamente;
- Usuário encontrado no banco de dados;
- O método retorna verdadeiro.

### Caso de Teste

```text
login = admin
senha = 123
```

### Resultado Esperado

```text
true
```

---

## Caminho 2 – Usuário Não Encontrado

### Fluxo

```text
1 → 2 → 3 → 4 → 5 → 8 → 10 → 11
```

### Descrição

- Conexão realizada com sucesso;
- Consulta executada corretamente;
- Nenhum usuário encontrado;
- O método retorna falso.

### Caso de Teste

```text
login = teste
senha = teste123
```

### Resultado Esperado

```text
false
```

---

## Caminho 3 – Exceção

### Fluxo

```text
1 → 2 → 3 → 9 → 10 → 11
```

### Descrição

- Ocorre erro durante a conexão ou execução da consulta;
- O fluxo é direcionado para o bloco de tratamento de exceções;
- O método retorna falso.

### Caso de Teste

```text
Banco de dados indisponível
```

### Resultado Esperado

```text
false
```

---

# 6. Melhorias Implementadas

Durante a revisão do código foram realizadas as seguintes melhorias:

### Segurança

- Correção da vulnerabilidade de SQL Injection;
- Implementação de `PreparedStatement`;
- Melhoria na organização das credenciais de conexão.

### Organização

- Remoção de variáveis globais desnecessárias;
- Melhor organização dos métodos e constantes.

### Tratamento de Erros

- Implementação de tratamento adequado de exceções;
- Exibição de mensagens de erro para facilitar manutenção.

### Gerenciamento de Recursos

- Implementação de `try-with-resources`;
- Fechamento automático de conexões e recursos do banco.

### Legibilidade

- Inserção de comentários explicativos;
- Simplificação da consulta SQL;
- Melhoria na organização geral do código.

---

# 7. Conclusão

A atividade permitiu aplicar conceitos fundamentais de teste estrutural, análise estática e revisão de código.

A construção do grafo de fluxo e o cálculo da complexidade ciclomática possibilitaram compreender melhor os caminhos de execução do programa e identificar os cenários necessários para a realização dos testes.

A principal dificuldade encontrada foi a identificação das vulnerabilidades presentes no código original e a modelagem correta do fluxo lógico do método analisado.

A revisão do código demonstrou que falhas aparentemente simples, como a concatenação de consultas SQL e o tratamento inadequado de exceções, podem gerar problemas significativos de segurança, manutenção e confiabilidade.

Conclui-se que a aplicação de boas práticas de programação, testes estruturais e revisões periódicas contribui diretamente para a qualidade, segurança e robustez dos sistemas de software, reduzindo riscos e facilitando futuras manutenções.
