

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

Legenda:
CM  = Caminho Mínimo (algoritmo de Dijkstra)
AGM = Árvore Geradora Mínima (algoritmos de Kruskal e Prim)

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

## 🌟 Recursos Extras

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
