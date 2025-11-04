package app;

import grafos.algorithms.Dijkstra;
import grafos.algorithms.KruskalMST;
import grafos.algorithms.PrimMST;
import grafos.io.GraphReader;
import grafos.model.Graph;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GraphGUI extends JFrame {
    
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JButton btnAddFiles;
    private JButton btnRun;
    private JButton btnExportCSV;
    private JSpinner sourceSpinner;
    private List<File> selectedFiles = new ArrayList<>();
    private JLabel lblCm;
    private JLabel lblK;
    private JLabel lblP;
    
    private NumberFormat nfInt = NumberFormat.getIntegerInstance(new Locale("pt", "BR"));
    private DecimalFormat dfTime = new DecimalFormat("0.0000");
    
    public GraphGUI() {
        setTitle("Analisador de Grafos - Dijkstra, Kruskal e Prim");
        setSize(1500, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setVisible(true);
        bringToFront();
        System.out.println("[GUI] Janela aberta");
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior - controles
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Controles"));
        
        btnAddFiles = new JButton("📁 Adicionar Arquivos .gr/.gr.gz");
        btnAddFiles.addActionListener(e -> addFiles());
        
        JLabel lblSource = new JLabel("Vértice fonte:");
        sourceSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000000, 1));
        
        btnRun = new JButton("▶️ Executar Análise");
        btnRun.setEnabled(false);
        btnRun.addActionListener(e -> runAnalysis());
        
        btnExportCSV = new JButton("💾 Exportar CSV");
        btnExportCSV.setEnabled(false);
        btnExportCSV.addActionListener(e -> exportCSV());
        
        topPanel.add(btnAddFiles);
        topPanel.add(lblSource);
        topPanel.add(sourceSpinner);
        topPanel.add(btnRun);
        topPanel.add(btnExportCSV);

    // Indicadores de últimos tempos
    JPanel timesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    lblCm = new JLabel("Dijkstra: — s");
    lblK = new JLabel("Kruskal: — s");
    lblP = new JLabel("Prim: — s");
    timesPanel.add(lblCm);
    timesPanel.add(new JLabel("|"));
    timesPanel.add(lblK);
    timesPanel.add(new JLabel("|"));
    timesPanel.add(lblP);
    topPanel.add(timesPanel);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Painel central - tabela de resultados
    String[] columns = {"Arquivo", "V", "A", "Dijkstra Custo", "Dijkstra(s)", "Kruskal", "K(s)", "Prim", "P(s)", "Alcance"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultTable.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 12));
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Larguras preferidas das colunas (permitindo scroll horizontal)
        int[] widths = {260, 80, 80, 140, 80, 140, 80, 140, 80, 100};
        for (int i = 0; i < widths.length; i++) {
            resultTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        
        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        add(tableScroll, BorderLayout.CENTER);
        
        // Painel inferior - log e progresso
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Log de Execução"));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Aguardando...");
        
        bottomPanel.add(logScroll, BorderLayout.CENTER);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void bringToFront() {
        try {
            setAlwaysOnTop(true);
            toFront();
            requestFocus();
        } catch (Exception ignore) {}
        finally {
            try { setAlwaysOnTop(false); } catch (Exception ignore) {}
        }
    }
    
    private void addFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("data"));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".gr") || f.getName().endsWith(".gr.gz");
            }
            @Override
            public String getDescription() {
                return "Arquivos de Grafo (*.gr, *.gr.gz)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File f : files) {
                if (!selectedFiles.contains(f)) {
                    selectedFiles.add(f);
                }
            }
            log("✓ Adicionados " + files.length + " arquivo(s)");
            log("Total de arquivos: " + selectedFiles.size());
            btnRun.setEnabled(!selectedFiles.isEmpty());
        }
    }
    
    private void runAnalysis() {
        tableModel.setRowCount(0); // Limpar resultados anteriores
        btnRun.setEnabled(false);
        btnAddFiles.setEnabled(false);
        btnExportCSV.setEnabled(false);
        
        int sourceVertex = (Integer) sourceSpinner.getValue();
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                grafos.io.GraphReader.VERBOSE = false;
                int total = selectedFiles.size();
                
                for (int i = 0; i < total; i++) {
                    File f = selectedFiles.get(i);
                    int progress = (int) ((i / (double) total) * 100);
                    setProgress(progress);
                    progressBar.setValue(progress);
                    progressBar.setString("Processando " + (i + 1) + "/" + total + ": " + f.getName());
                    
                    try {
                        log("\n📖 Lendo: " + f.getName());
                        long tStart = System.nanoTime();
                        Graph g = GraphReader.readGraph(f.getAbsolutePath());
                        long tEnd = System.nanoTime();
                        double tempoLeitura = (tEnd - tStart) / 1e9;
                        log("  ✓ Leitura: " + dfTime.format(tempoLeitura) + " s");
                        
                        int n = g.n;
                        int m = g.arcCount;
                        
                        log("  ⚡ Dijkstra...");
                        long t1 = System.nanoTime();
                        long[] dist = Dijkstra.dijkstra(g, sourceVertex);
                        long custoD = Dijkstra.totalDistance(dist);
                        long t2 = System.nanoTime();
                        double tempoD = (t2 - t1) / 1e9;
                        log("     ✓ " + dfTime.format(tempoD) + " s");
                        final double uiTempoD = tempoD;
                        SwingUtilities.invokeLater(() -> lblCm.setText("Dijkstra: " + dfTime.format(uiTempoD) + " s"));
                        
                        int reach = 0;
                        for (int j = 1; j < dist.length; j++) if (dist[j] < Long.MAX_VALUE) reach++;
                        
                        log("  🌲 Kruskal...");
                        t1 = System.nanoTime();
                        long custoK = KruskalMST.mstCost(g);
                        t2 = System.nanoTime();
                        double tempoK = (t2 - t1) / 1e9;
                        log("     ✓ " + dfTime.format(tempoK) + " s");
                        final double uiTempoK = tempoK;
                        SwingUtilities.invokeLater(() -> lblK.setText("Kruskal: " + dfTime.format(uiTempoK) + " s"));
                        
                        log("  🌲 Prim...");
                        t1 = System.nanoTime();
                        long custoP = PrimMST.mstCost(g, sourceVertex);
                        t2 = System.nanoTime();
                        double tempoP = (t2 - t1) / 1e9;
                        log("     ✓ " + dfTime.format(tempoP) + " s");
                        final double uiTempoP = tempoP;
                        SwingUtilities.invokeLater(() -> lblP.setText("Prim: " + dfTime.format(uiTempoP) + " s"));
                        
                        if (custoK != custoP) {
                            log("  ⚠️ Aviso: Kruskal ≠ Prim");
                        }
                        
                        // Adicionar linha na tabela
                        final int finalN = n;
                        final int finalM = m;
                        final long finalCustoD = custoD;
                        final double finalTempoD = tempoD;
                        final long finalCustoK = custoK;
                        final double finalTempoK = tempoK;
                        final long finalCustoP = custoP;
                        final double finalTempoP = tempoP;
                        final int finalReach = reach;
                        final String fileName = f.getName();
                        
                        SwingUtilities.invokeLater(() -> {
                            tableModel.addRow(new Object[]{
                                fileName,
                                nfInt.format(finalN),
                                nfInt.format(finalM),
                                nfInt.format(finalCustoD),
                                dfTime.format(finalTempoD),
                                nfInt.format(finalCustoK),
                                dfTime.format(finalTempoK),
                                nfInt.format(finalCustoP),
                                dfTime.format(finalTempoP),
                                nfInt.format(finalReach)
                            });
                        });
                        
                    } catch (Exception ex) {
                        log("  ✗ ERRO: " + ex.getMessage());
                    }
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                progressBar.setValue(100);
                progressBar.setString("✓ Análise concluída!");
                log("\n✓ Processamento completo!");
                btnRun.setEnabled(true);
                btnAddFiles.setEnabled(true);
                btnExportCSV.setEnabled(tableModel.getRowCount() > 0);
            }
        };
        
        worker.execute();
    }
    
    private void exportCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("results.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                pw.println("file,V,arcs,dijkstra_cost,dijkstra_time_s,krus_cost,krus_time_s,prim_cost,prim_time_s,reachable");
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        String val = tableModel.getValueAt(i, j).toString().replace(".", "").replace(",", ".");
                        pw.print(val);
                        if (j < tableModel.getColumnCount() - 1) pw.print(",");
                    }
                    pw.println();
                }
                
                log("✓ CSV exportado: " + fileChooser.getSelectedFile().getName());
                JOptionPane.showMessageDialog(this, "CSV exportado com sucesso!", "Exportar", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                log("✗ Erro ao exportar CSV: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Erro ao exportar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GraphGUI());
    }
}
