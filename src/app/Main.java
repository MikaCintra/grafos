package app;

import grafos.algorithms.Dijkstra;
import grafos.algorithms.KruskalMST;
import grafos.algorithms.PrimMST;
import grafos.io.GraphReader;
import grafos.model.Graph;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Main {

    // ====================== TESTES ======================
    public static void runTests() {
        System.out.println("\nExecutando testes...");
        Graph g = new Graph(5);
        g.addUndirectedEdge(1, 2, 2);
        g.addUndirectedEdge(1, 3, 4);
        g.addUndirectedEdge(2, 3, 1);
        g.addUndirectedEdge(2, 4, 3);
        g.addUndirectedEdge(3, 4, 5);
        g.addUndirectedEdge(3, 5, 7);
        g.addUndirectedEdge(4, 5, 2);

        System.out.println("\nTeste 1: Verificando Dijkstra");
        long[] dist = Dijkstra.dijkstra(g, 1);
        boolean dijkstraOk = dist[2] == 2 && dist[3] == 3 && dist[4] == 5 && dist[5] == 7;
        System.out.println("Distâncias a partir do vértice 1: ");
        for (int i = 1; i <= 5; i++) System.out.printf("Até %d: %d\n", i, dist[i]);
        System.out.println("Teste Dijkstra: " + (dijkstraOk ? "PASSOU" : "FALHOU"));

        System.out.println("\nTeste 2: Verificando Kruskal");
        long kruskalCost = KruskalMST.mstCost(g);
        boolean kruskalOk = kruskalCost == 8;
        System.out.println("Custo da AGM (Kruskal): " + kruskalCost);
        System.out.println("Teste Kruskal: " + (kruskalOk ? "PASSOU" : "FALHOU"));

        System.out.println("\nTeste 3: Verificando Prim");
        long primCost = PrimMST.mstCost(g, 1);
        boolean primOk = primCost == 8;
        System.out.println("Custo da AGM (Prim): " + primCost);
        System.out.println("Teste Prim: " + (primOk ? "PASSOU" : "FALHOU"));

        boolean todosTestesPassaram = dijkstraOk && kruskalOk && primOk;
        System.out.println("\nResultado final dos testes: " + (todosTestesPassaram ? "TODOS OS TESTES PASSARAM" : "ALGUNS TESTES FALHARAM"));
        System.out.println("============================================");
    }

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando o programa...");

            int sourceVertex = 1;
            String csvPath = null;
            List<String> cliFiles = new ArrayList<>();
            boolean runTests = true;
            boolean quiet = false; // controla logs de leitura
            boolean useConsole = false; // por padrão abre GUI

            for (int i = 0; i < args.length; i++) {
                String a = args[i];
                if ("--gui".equals(a)) {
                    useConsole = false; // força GUI
                } else if ("--console".equals(a)) {
                    useConsole = true; // força modo console
                } else if ("--csv".equals(a) && i + 1 < args.length) {
                    csvPath = args[++i];
                    useConsole = true; // CSV implica console
                } else if ("--source".equals(a) && i + 1 < args.length) {
                    try { sourceVertex = Integer.parseInt(args[++i]); } catch (NumberFormatException ex) { }
                    useConsole = true; // source customizado implica console
                } else if ("--no-tests".equals(a)) {
                    runTests = false;
                } else if ("--quiet".equals(a)) {
                    quiet = true;
                    useConsole = true; // quiet implica console
                } else if ("--verbose".equals(a)) {
                    quiet = false;
                    useConsole = true; // verbose implica console
                } else if (a != null && a.startsWith("--")) {
                    // ignore unknown flags
                } else if (a != null && !a.isEmpty()) {
                    cliFiles.add(a);
                    useConsole = true; // arquivos CLI implicam console
                }
            }

            // Se nenhum argumento de console foi passado, abre GUI por padrão
            if (!useConsole) {
                SwingUtilities.invokeLater(() -> new GraphGUI());
                return;
            }

            if (runTests) runTests();

            String[] files;
            if (cliFiles.isEmpty()) {
                files = new String[]{
                    "data/USA-road-d.NY.gr.gz",
                    "data/USA-road-d.BAY.gr.gz",
                    "data/USA-road-d.COL.gr.gz"
                };
            } else {
                files = cliFiles.toArray(new String[0]);
            }

            System.out.println("Legenda:");
            System.out.println("CM  = Caminho Mínimo (algoritmo de Dijkstra)");
            System.out.println("AGM = Árvore Geradora Mínima (algoritmos de Kruskal e Prim)");
            System.out.println();

            NumberFormat nfInt = NumberFormat.getIntegerInstance(Locale.of("pt", "BR"));
            DecimalFormat dfTime = new DecimalFormat("0.0000");

            System.out.printf("%10s %10s %15s %10s %15s %10s %15s %10s %10s%n",
                    "V", "A", "CM Custo", "CM(s)", "Kruskal", "K(s)", "Prim", "P(s)", "Alcance");
            String separator = String.join("", Collections.nCopies(110, "="));
            System.out.println(separator);

            PrintWriter csv = null;
            if (csvPath != null) {
                try {
                    boolean novo = !(new File(csvPath).exists());
                    csv = new PrintWriter(new FileWriter(csvPath, true));
                    if (novo) csv.println("file;V;arcs;dijkstra_cost;dijkstra_time_s;krus_cost;krus_time_s;prim_cost;prim_time_s;reachable;read_time_s;density_arcs;mst_equal");
                } catch (IOException ioe) {
                    System.out.println("Aviso: não foi possível abrir CSV para escrita: " + ioe.getMessage());
                }
            }

            for (String f : files) {
                try {
                    System.out.println(separator);
                    System.out.println("Processando grafo: " + f + " (fonte=" + sourceVertex + ")");
                    grafos.io.GraphReader.VERBOSE = !quiet;
                    if (!new java.io.File(f).exists()) {
                        System.out.println("ERRO: Arquivo não encontrado: " + f);
                        continue;
                    }
                    
                    System.out.print("📖 Lendo grafo...");
                    long tStart = System.nanoTime();
                    Graph g = GraphReader.readGraph(f);
                    long tEnd = System.nanoTime();
                    double tempoLeitura = (tEnd - tStart) / 1e9;
                    System.out.printf(" ✓ concluído em %s s%n", dfTime.format(tempoLeitura));

                    int n = g.n;
                    int m = g.arcCount;
                    double density = n > 0 ? ((double)m)/n : 0.0;

                    System.out.print("⚡ Executando Dijkstra (CM)...");
                    long t1 = System.nanoTime();
                    long[] dist = Dijkstra.dijkstra(g, sourceVertex);
                    long custoD = grafos.algorithms.Dijkstra.totalDistance(dist);
                    long t2 = System.nanoTime();
                    double tempoD = (t2 - t1) / 1e9;
                    System.out.printf(" ✓ %.4f s%n", tempoD);

                    int reach = 0;
                    for (int i = 1; i < dist.length; i++) if (dist[i] < Long.MAX_VALUE) reach++;

                    System.out.print("🌲 Executando Kruskal (AGM)...");
                    t1 = System.nanoTime();
                    long custoK = KruskalMST.mstCost(g);
                    t2 = System.nanoTime();
                    double tempoK = (t2 - t1) / 1e9;
                    System.out.printf(" ✓ %.4f s%n", tempoK);

                    System.out.print("🌲 Executando Prim (AGM)...");
                    t1 = System.nanoTime();
                    long custoP = PrimMST.mstCost(g, sourceVertex);
                    t2 = System.nanoTime();
                    double tempoP = (t2 - t1) / 1e9;
                    System.out.printf(" ✓ %.4f s%n", tempoP);

                    if (custoK != custoP) {
                        System.out.println("⚠️  Aviso: custos de AGM diferentes entre Kruskal e Prim: " + custoK + " vs " + custoP);
                    }
                    
                    System.out.println("\n📊 Resultados:");
                    System.out.printf("%10s %10s %15s %10s %15s %10s %15s %10s %10s%n",
                            nfInt.format(n), nfInt.format(m),
                            nfInt.format(custoD), dfTime.format(tempoD),
                            nfInt.format(custoK), dfTime.format(tempoK),
                            nfInt.format(custoP), dfTime.format(tempoP),
                            nfInt.format(reach));

                    // barras ASCII proporcionais aos tempos (máx -> 30 colunas)
                    double maxT = Math.max(tempoD, Math.max(tempoK, tempoP));
                    int width = 30;
                    String barD = asciiBar(tempoD, maxT, width);
                    String barK = asciiBar(tempoK, maxT, width);
                    String barP = asciiBar(tempoP, maxT, width);
                    System.out.println("  CM      : " + barD + " " + dfTime.format(tempoD) + " s");
                    System.out.println("  Kruskal : " + barK + " " + dfTime.format(tempoK) + " s");
                    System.out.println("  Prim    : " + barP + " " + dfTime.format(tempoP) + " s");

                    if (csv != null) {
                        try {
                            // Formatar números com separador brasileiro (vírgula decimal)
                            String custoDS = nfInt.format(custoD);
                            String tempoDFormatted = dfTime.format(tempoD).replace(".", ",");
                            String custoKS = nfInt.format(custoK);
                            String tempoKFormatted = dfTime.format(tempoK).replace(".", ",");
                            String custoPS = nfInt.format(custoP);
                            String tempoPFormatted = dfTime.format(tempoP).replace(".", ",");
                            String reachS = nfInt.format(reach);
                            String tempoLeituraFormatted = String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.3f", tempoLeitura).replace(".", ",");
                            String densityFormatted = String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.6f", density).replace(".", ",");
                            
                            csv.printf("%s;%d;%d;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s%n",
                                    f, n, m, custoDS, tempoDFormatted, custoKS, tempoKFormatted, custoPS, tempoPFormatted, reachS, tempoLeituraFormatted, densityFormatted, (custoK==custoP?"Sim":"Não"));
                            csv.flush();
                        } catch (Exception ex) {
                            System.out.println("Aviso: falha ao escrever CSV: " + ex.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("ERRO ao processar arquivo " + f + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (csv != null) { try { csv.close(); } catch (Exception ignore) {} }

            System.out.println("\nExecução concluída. Utilize --csv para gerar relatório tabular e --quiet para reduzir logs.");
            System.out.println("Para interface gráfica, use: java app.Main --gui");
        } catch (Exception e) {
            System.out.println("ERRO geral: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String asciiBar(double value, double max, int width) {
        if (max <= 0) max = 1.0;
        int filled = (int)Math.round((value / max) * width);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) sb.append(i < filled ? '█' : ' ');
        return '[' + sb.toString() + ']';
    }
}
