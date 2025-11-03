# Projeto de Grafos – Algoritmos de Caminho Mínimo e Árvore Geradora Mínima

# Projeto: Algoritmos de Grafos – Dijkstra, Kruskal e Prim

> Implementação em Java dos algoritmos de Dijkstra, Kruskal e Prim para análise de redes de estradas dos EUA

Implementação em Java dos algoritmos clássicos de grafos para o trabalho de Grafos:

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)- Dijkstra (caminho mínimo)

- Kruskal (árvore geradora mínima)

---- Prim (árvore geradora mínima)



## 📋 Sumário## Estrutura de Pastas



- [Sobre o Projeto](#sobre-o-projeto)```

- [Algoritmos Implementados](#algoritmos-implementados)ProjetoGrafos/

- [Estrutura do Projeto](#estrutura-do-projeto)├── src/                → Código-fonte (Main.java)

- [Instalação e Configuração](#instalação-e-configuração)├── data/               → Arquivos de entrada .gr/.gr.gz

- [Como Usar](#como-usar)├── results/            → Espaço para salvar saídas (opcional)

- [Resultados](#resultados)├── .vscode/            → Configurações do VS Code

- [Recursos Extras](#recursos-extras)└── README.md           → Este arquivo

- [Tecnologias Utilizadas](#tecnologias-utilizadas)```



---## Como usar



## 🎯 Sobre o Projeto1. Baixe os grafos do 9th DIMACS Challenge em:

   - https://www.diag.uniroma1.it/challenge9/download.shtml

Este projeto implementa e analisa três algoritmos clássicos de teoria dos grafos:

2. Coloque na pasta `data/` os arquivos:

1. **Dijkstra** – Cálculo de caminho mínimo a partir de uma fonte   - `USA-road-d.NY.gr.gz`

2. **Kruskal** – Árvore Geradora Mínima (AGM) usando Union-Find   - `USA-road-d.BAY.gr.gz`

3. **Prim** – Árvore Geradora Mínima (AGM) usando fila de prioridade   - `USA-road-d.COL.gr.gz`

Os algoritmos são testados em grafos reais de redes de estradas dos EUA, fornecidos pelo **9th DIMACS Implementation Challenge**.

3. Abra a pasta `ProjetoGrafos` no VS Code.

4. Compile e execute:

### Datasets Utilizados   - Pelo VS Code: Run → Run Without Debugging (ou botão "Run Java" no `Main`)

   - Ou pelo terminal:

- **NY** (New York) – 264.346 vértices, 733.846 arcos     ```bash

- **BAY** (San Francisco Bay Area) – 321.270 vértices, 800.172 arcos     javac -d bin src/Main.java

- **COL** (Colorado) – 435.666 vértices, 1.057.066 arcos     java -cp bin Main

     ```

Fonte: [9th DIMACS Challenge - Shortest Paths](https://www.diag.uniroma1.it/challenge9/download.shtml)

5. A saída no terminal mostrará, para cada instância:

---   - número de vértices (V)

   - número de arestas (E)

## ⚙️ Algoritmos Implementados   - custo e tempo de Dijkstra, Kruskal e Prim.


### 1. Dijkstra (Caminho Mínimo)
- **Arquivo:** `src/grafos/algorithms/Dijkstra.java`
- **Complexidade:** O((V + E) log V) com PriorityQueue
- **Função:** Calcula distâncias mínimas de um vértice fonte para todos os demais
- **Saída:** Custo total (soma das distâncias) e tempo de execução

### 2. Kruskal (AGM)
- **Arquivos:** `src/grafos/algorithms/KruskalMST.java`, `UnionFind.java`
- **Complexidade:** O(E log E) com ordenação de arestas
- **Função:** Encontra árvore geradora mínima usando Union-Find
- **Otimização:** Compressão de caminho e união por rank

### 3. Prim (AGM)
- **Arquivo:** `src/grafos/algorithms/PrimMST.java`
- **Complexidade:** O((V + E) log V) com PriorityQueue
- **Função:** Constrói AGM incrementalmente a partir de um vértice inicial
- **Implementação:** Fila de prioridade de arestas candidatas

---

## 📁 Estrutura do Projeto

```
Projeto Grafos/
├── src/
│   ├── app/
│   │   ├── Main.java              # Ponto de entrada (console)
│   │   └── GraphGUI.java          # Interface gráfica Swing
│   └── grafos/
│       ├── algorithms/
│       │   ├── Dijkstra.java      # Algoritmo de caminho mínimo
│       │   ├── KruskalMST.java    # Algoritmo de Kruskal
│       │   ├── PrimMST.java       # Algoritmo de Prim
│       │   └── UnionFind.java     # Estrutura Union-Find
│       ├── io/
│       │   └── GraphReader.java   # Leitor DIMACS .gr/.gr.gz
│       └── model/
│           ├── Edge.java          # Representação de aresta
│           └── Graph.java         # Estrutura do grafo
├── data/
│   ├── USA-road-d.NY.gr.gz       # Dataset New York
│   ├── USA-road-d.BAY.gr.gz      # Dataset Bay Area
│   └── USA-road-d.COL.gr.gz      # Dataset Colorado
├── bin/                           # Classes compiladas
├── results_console.csv            # Resultados exportados (formato BR)
├── TABELA_RESULTADOS.md           # Tabela formatada para relatório
├── FORMATO_CSV.md                 # Comparação formatos CSV (BR vs US)
├── IMPORTAR_CSV.md                # Guia de importação (Excel/LibreOffice)
├── Instrucoes_Projeto_Grafos.md   # Especificação do projeto
└── README.md                      # Este arquivo
```

---

## 🚀 Instalação e Configuração

### Pré-requisitos

- **Java JDK 11+** (testado com Java 17)
- **VS Code** (opcional, mas recomendado)
  - Extension Pack for Java

### Passos de Instalação

1. **Clone ou baixe o projeto**
   ```bash
   cd "C:\Users\cintr\OneDrive\Área de Trabalho\Projeto Grafos"
   ```

2. **Baixe os datasets** (se ainda não tiver)
   - Acesse: https://www.diag.uniroma1.it/challenge9/download.shtml
   - Baixe os arquivos `.gr.gz` de NY, BAY e COL (coluna "Distance graph")
   - Coloque os arquivos na pasta `data/`

3. **Compile o projeto**
   ```powershell
   # PowerShell
   $files = Get-ChildItem -Recurse -Path src/grafos,src/app -Filter *.java | ForEach-Object { $_.FullName }
   javac -d bin -cp bin $files
   ```

   Ou use a task do VS Code:
   - `Ctrl+Shift+B` → "Compilar Projeto (pacotes)"

---

## 💻 Como Usar

### Modo Console (Padrão)

Executa análise completa dos três datasets:

```powershell
java -cp bin app.Main
```

**Saída:**
```
Executando testes...
[resultados dos testes unitários]

Legenda:
CM  = Caminho Mínimo (algoritmo de Dijkstra)
AGM = Árvore Geradora Mínima (algoritmos de Kruskal e Prim)

         V          A        Dijkstra Custo      Dijkstra(s)         Kruskal       K(s)            Prim       P(s)    Alcance
====================================================================================================================
Processando grafo: data/USA-road-d.NY.gr.gz (fonte=1)
📖 Lendo grafo... ✓ concluído em 8,2711 s
⚡ Executando Dijkstra (CM)... ✓ 1,2884 s
🌲 Executando Kruskal (AGM)... ✓ 1,3595 s
🌲 Executando Prim (AGM)... ✓ 2,1650 s

📊 Resultados:
   264.346    733.846 186.686.642.878     1,2884     261.159.288     1,3595     261.159.288     2,1650    264.346
  Dijkstra : [████████████████████████      ] 1,2884 s
  Kruskal  : [█████████████████████████     ] 1,3595 s
  Prim     : [██████████████████████████████] 2,1650 s
...
```

### Modo Silencioso

Reduz logs de leitura:

```powershell
java -cp bin app.Main --quiet
```

### Exportar CSV

Gera arquivo CSV com todos os resultados em **formato brasileiro** (compatível com Excel pt-BR):

```powershell
java -cp bin app.Main --quiet --csv results.csv
```

**Formato do CSV:**
- ✅ **Delimitador:** ponto-e-vírgula (`;`)
- ✅ **Decimal:** vírgula (`,`) → ex: `1,3901`
- ✅ **Milhar:** ponto (`.`) → ex: `186.686.642.878`
- ✅ **Booleano:** `Sim`/`Não` (português)

**Colunas do CSV:**
- `file`, `V`, `arcs`
- `dijkstra_cost`, `dijkstra_time_s`
- `krus_cost`, `krus_time_s`
- `prim_cost`, `prim_time_s`
- `reachable`, `read_time_s`, `density_arcs`, `mst_equal`

> 💡 **Dica:** Abre automaticamente no Excel duplo-clicando! Veja [IMPORTAR_CSV.md](IMPORTAR_CSV.md) para mais detalhes.

### Modo Gráfico (GUI)

Interface Swing completa:

```powershell
java -cp bin app.Main --gui
```

**Recursos da GUI:**
- 📁 Seleção visual de arquivos
- ▶️ Execução com barra de progresso
- 📊 Tabela de resultados formatada
- 📝 Log em tempo real
- 💾 Exportação CSV integrada

### Opções Avançadas

```powershell
# Alterar vértice fonte
java -cp bin app.Main --source 42

# Pular testes unitários
java -cp bin app.Main --no-tests

# Combinar opções
java -cp bin app.Main --quiet --source 10 --csv custom.csv
```

---

## 📊 Resultados

### Tabela de Resultados

| Instância | Vértices (|V|) | Arestas (|E|) | CM – Dijkstra (custo) | CM – Dijkstra (s) | AGM – Kruskal (custo) | AGM – Kruskal (s) | AGM – Prim (custo) | AGM – Prim (s) |
|-----------|---------------:|---------------:|----------------------:|------------------:|----------------------:|------------------:|-------------------:|---------------:|
| **NY**    | 264.346       | 733.846       | 186.686.642.878      | 1,2884           | 261.159.288          | 1,3595           | 261.159.288       | 2,1650        |
| **BAY**   | 321.270       | 800.172       | 295.629.435.062      | 1,2349           | 435.798.417          | 4,1768           | 435.798.417       | 0,3389        |
| **COL**   | 435.666       | 1.057.066     | 1.104.876.702.568    | 0,3628           | 1.323.900.090        | 0,8084           | 1.323.900.090     | 0,7872        |

### Observações Importantes

✅ **Corretude:** Kruskal e Prim produzem AGMs de custo idêntico (validação cruzada)  
✅ **Alcançabilidade:** 100% dos vértices alcançáveis a partir da fonte em todos os grafos  
✅ **Consistência:** Testes unitários passam validando algoritmos em grafo sintético  

Veja análise completa em: **[TABELA_RESULTADOS.md](TABELA_RESULTADOS.md)**

---

## 🌟 Recursos Extras

Este projeto vai **muito além** dos requisitos mínimos:

### Interface Gráfica (Swing)
- ✅ Janela interativa com seleção de arquivos
- ✅ Barra de progresso em tempo real
- ✅ Tabela de resultados com scroll
- ✅ Log visual com emojis (📖 ⚡ 🌲 ✓)
- ✅ Exportação CSV integrada

### Linha de Comando Avançada
- ✅ `--quiet` / `--verbose` – Controle de verbosidade
- ✅ `--csv <arquivo>` – Exportação automática
- ✅ `--source <vértice>` – Fonte configurável
- ✅ `--no-tests` – Pular testes unitários
- ✅ `--gui` – Lançar interface gráfica

### Qualidade de Código
- ✅ **Testes unitários** integrados (smoke tests)
- ✅ **Arquitetura modular** (5 pacotes organizados)
- ✅ **Separação conceitual** (grafo dirigido vs não-dirigido)
- ✅ **Formatação visual** (separadores de milhar, barras ASCII)

### Métricas Adicionais
- ✅ Alcance (vértices alcançáveis)
- ✅ Tempo de leitura de arquivo
- ✅ Densidade do grafo (arcos/vértice)
- ✅ Validação Kruskal = Prim

---

## 🛠️ Tecnologias Utilizadas

- **Java 17** – Linguagem de programação
- **Swing** – Interface gráfica (javax.swing)
- **java.util.PriorityQueue** – Heap para Dijkstra e Prim
- **java.util.zip.GZIPInputStream** – Leitura de arquivos .gz
- **VS Code** – Ambiente de desenvolvimento
- **DIMACS Format** – Formato de entrada de grafos

### Estruturas de Dados

- **Lista de Adjacência** – Representação de grafo (directed/undirected)
- **Priority Queue (Min-Heap)** – Dijkstra e Prim
- **Union-Find com compressão de caminho** – Kruskal
- **ArrayList de Arestas** – Ordenação para Kruskal

---

## 📝 Comandos Rápidos

```powershell
# Compilar
$files = Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin -cp bin $files

# Executar modo padrão
java -cp bin app.Main

# Executar silencioso + CSV
java -cp bin app.Main --quiet --csv results.csv

# Abrir GUI
java -cp bin app.Main --gui

# Executar GUI diretamente
java -cp bin app.GraphGUI
```

---

## 📖 Documentação Adicional

- **[Instrucoes_Projeto_Grafos.md](Instrucoes_Projeto_Grafos.md)** – Especificação completa do projeto
- **[TABELA_RESULTADOS.md](TABELA_RESULTADOS.md)** – Resultados formatados e análises
- **[results_console.csv](results_console.csv)** – Dados brutos em CSV

---

## 👥 Autores

**Grupo:** [Preencher conforme Instrucoes_Projeto_Grafos.md]

- Integrante 1 – RA: _______
- Integrante 2 – RA: _______
- Integrante 3 – RA: _______

---

## 📅 Informações do Projeto

- **Disciplina:** _______________________
- **Professor:** _______________________
- **Data de Execução:** 3 de novembro de 2025
- **Versão Java:** 17+
- **Ambiente:** Windows 10/11

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

---

## 🎓 Referências

1. **9th DIMACS Implementation Challenge – Shortest Paths**  
   https://www.diag.uniroma1.it/challenge9/

2. **Cormen, T. H., et al.** *Introduction to Algorithms* (3rd ed.)  
   - Capítulo 24: Single-Source Shortest Paths (Dijkstra)
   - Capítulo 23: Minimum Spanning Trees (Kruskal, Prim)

3. **DIMACS Graph Format Specification**  
   https://www.diag.uniroma1.it/challenge9/format.shtml

---

<p align="center">
  Desenvolvido com ☕ e 💻 para o curso de Algoritmos em Grafos
</p>
#   g r a f o s  
 