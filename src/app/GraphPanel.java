package app;

import grafos.model.Edge;
import grafos.model.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

public class GraphPanel extends JPanel {
    
    private Graph graph;
    private final Map<Integer, Point> nodePositions;
    private long[] distances; // Para colorir baseado em Dijkstra
    private int sourceVertex = 1;
    
    public GraphPanel() {
        setPreferredSize(new Dimension(600, 500));
        setMinimumSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        nodePositions = new HashMap<>();
        
        // Recalcular posições quando o componente for redimensionado
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (graph != null) {
                    calculateNodePositions();
                    repaint();
                }
            }
        });
    }
    
    public void setGraph(Graph g, long[] dijkstraDist, int source) {
        System.out.println("[GraphPanel] setGraph chamado: n=" + g.n + ", source=" + source);
        this.graph = g;
        this.distances = dijkstraDist;
        this.sourceVertex = source;
        calculateNodePositions();
        System.out.println("[GraphPanel] Posições calculadas: " + nodePositions.size() + " nós");
        repaint();
    }
    
    public void clearGraph() {
        this.graph = null;
        this.distances = null;
        this.nodePositions.clear();
        repaint();
    }
    
    private String messageTitle = null;
    private String messageSubtitle = null;
    private String messageDetail = null;
    
    public void showMessage(String title, String subtitle, String detail) {
        this.graph = null;
        this.messageTitle = title;
        this.messageSubtitle = subtitle;
        this.messageDetail = detail;
        repaint();
    }
    
    private void calculateNodePositions() {
        if (graph == null) return;
        
        nodePositions.clear();
        int n = graph.n;
        
        // Layout circular para visualização
        int width = getWidth() > 0 ? getWidth() : 600;
        int height = getHeight() > 0 ? getHeight() : 500;
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY) - 50;
        
        if (n <= 50) {
            // Layout circular para grafos pequenos
            for (int i = 1; i <= n; i++) {
                double angle = 2 * Math.PI * (i - 1) / n;
                int x = centerX + (int)(radius * Math.cos(angle));
                int y = centerY + (int)(radius * Math.sin(angle));
                nodePositions.put(i, new Point(x, y));
            }
        } else {
            // Layout em grade para grafos maiores
            int cols = (int) Math.ceil(Math.sqrt(n));
            int cellWidth = width / (cols + 1);
            int cellHeight = height / (cols + 1);
            
            for (int i = 1; i <= n; i++) {
                int row = (i - 1) / cols;
                int col = (i - 1) % cols;
                int x = cellWidth * (col + 1);
                int y = cellHeight * (row + 1);
                nodePositions.put(i, new Point(x, y));
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        System.out.println("[GraphPanel] paintComponent: graph=" + (graph != null ? graph.n : "null") + ", size=" + getWidth() + "x" + getHeight());
        
        if (graph == null) {
            if (messageTitle != null) {
                // Mensagem customizada
                int centerY = getHeight() / 2;
                
                g2d.setColor(new Color(200, 100, 0));
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(messageTitle)) / 2;
                g2d.drawString(messageTitle, x, centerY - 40);
                
                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font("Arial", Font.PLAIN, 13));
                fm = g2d.getFontMetrics();
                x = (getWidth() - fm.stringWidth(messageSubtitle)) / 2;
                g2d.drawString(messageSubtitle, x, centerY - 10);
                
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                fm = g2d.getFontMetrics();
                x = (getWidth() - fm.stringWidth(messageDetail)) / 2;
                g2d.drawString(messageDetail, x, centerY + 15);
            } else {
                // Mensagem padrão
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String msg = "Clique em uma linha da tabela para visualizar";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(msg)) / 2;
                int y = getHeight() / 2;
                g2d.drawString(msg, x, y);
            }
            return;
        }
        
        // Recalcular posições se estiverem vazias (primeira renderização)
        if (nodePositions.isEmpty()) {
            calculateNodePositions();
        }
        
        // Desenhar arestas primeiro
        drawEdges(g2d);
        
        // Desenhar vértices por cima
        drawNodes(g2d);
        
        // Informações
        drawInfo(g2d);
    }
    
    private void drawEdges(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(new Color(200, 200, 200, 150));
        
        // Usar adjacência dirigida para desenhar arestas
        for (int u = 1; u <= graph.n; u++) {
            Point pU = nodePositions.get(u);
            if (pU == null) continue;
            
            for (Edge edge : graph.adjDir.get(u)) {
                Point pV = nodePositions.get(edge.v);
                if (pV == null) continue;
                
                g2d.draw(new Line2D.Double(pU.x, pU.y, pV.x, pV.y));
            }
        }
    }
    
    private void drawNodes(Graphics2D g2d) {
        int nodeSize = graph.n <= 50 ? 12 : 6;
        
        for (int i = 1; i <= graph.n; i++) {
            Point p = nodePositions.get(i);
            if (p == null) continue;
            
            // Colorir baseado na distância do Dijkstra
            Color nodeColor;
            if (i == sourceVertex) {
                nodeColor = new Color(255, 0, 0); // Vermelho para origem
            } else if (distances != null && distances[i] < Long.MAX_VALUE) {
                // Verde para vértices alcançáveis (gradiente baseado na distância)
                nodeColor = new Color(0, 150, 0);
            } else {
                nodeColor = new Color(100, 100, 100); // Cinza para não alcançáveis
            }
            
            g2d.setColor(nodeColor);
            g2d.fill(new Ellipse2D.Double(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize));
            
            // Borda
            g2d.setColor(Color.BLACK);
            g2d.draw(new Ellipse2D.Double(p.x - nodeSize/2, p.y - nodeSize/2, nodeSize, nodeSize));
            
            // Mostrar número do vértice apenas para grafos pequenos
            if (graph.n <= 30) {
                g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                String label = String.valueOf(i);
                FontMetrics fm = g2d.getFontMetrics();
                int w = fm.stringWidth(label);
                g2d.drawString(label, p.x - w/2, p.y - nodeSize/2 - 2);
            }
        }
    }
    
    private void drawInfo(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        
        String info = String.format("Vértices: %d | Arcos: %d | Origem: %d", 
                                    graph.n, graph.arcCount, sourceVertex);
        g2d.drawString(info, 10, 20);
        
        // Legenda
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int legendY = getHeight() - 40;
        
        g2d.setColor(Color.RED);
        g2d.fillOval(10, legendY, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Origem", 25, legendY + 9);
        
        g2d.setColor(new Color(0, 150, 0));
        g2d.fillOval(10, legendY + 15, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Alcançável", 25, legendY + 24);
        
        g2d.setColor(Color.GRAY);
        g2d.fillOval(10, legendY + 30, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Não alcançável", 25, legendY + 39);
    }
}
