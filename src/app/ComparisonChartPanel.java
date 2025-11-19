package app;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class ComparisonChartPanel extends JPanel {
    
    private List<ChartData> dataList = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("0.0000");
    private String chartType = "time"; // "time" ou "size"
    
    public static class ChartData {
        String fileName;
        int vertices;
        int arcs;
        double dijkstraTime;
        double kruskalTime;
        double primTime;
        
        public ChartData(String fileName, int vertices, int arcs, 
                        double dijkstraTime, double kruskalTime, double primTime) {
            this.fileName = fileName;
            this.vertices = vertices;
            this.arcs = arcs;
            this.dijkstraTime = dijkstraTime;
            this.kruskalTime = kruskalTime;
            this.primTime = primTime;
        }
    }
    
    public ComparisonChartPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 500));
    }
    
    public void setChartType(String type) {
        this.chartType = type;
        repaint();
    }
    
    public void updateData(List<ChartData> data) {
        this.dataList = new ArrayList<>(data);
        repaint();
    }
    
    public void clearData() {
        this.dataList.clear();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (dataList.isEmpty()) {
            drawEmptyMessage(g2d);
            return;
        }
        
        if ("time".equals(chartType)) {
            drawTimeComparisonChart(g2d);
        } else if ("size".equals(chartType)) {
            drawSizeChart(g2d);
        }
    }
    
    private void drawEmptyMessage(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String msg = "Execute a análise para ver o gráfico comparativo";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(msg)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(msg, x, y);
    }
    
    private void drawTimeComparisonChart(Graphics2D g2d) {
        int margin = 60;
        int chartWidth = getWidth() - 2 * margin;
        int chartHeight = getHeight() - 2 * margin - 60;
        int chartX = margin;
        int chartY = margin;
        
        // Título
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.BLACK);
        String title = "Comparação de Tempos de Execução";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 25);
        
        // Encontrar tempo máximo para escala
        double maxTime = 0;
        for (ChartData data : dataList) {
            maxTime = Math.max(maxTime, data.dijkstraTime);
            maxTime = Math.max(maxTime, data.kruskalTime);
            maxTime = Math.max(maxTime, data.primTime);
        }
        
        if (maxTime == 0) maxTime = 1;
        
        // Desenhar eixos
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight); // Eixo X
        g2d.drawLine(chartX, chartY, chartX, chartY + chartHeight); // Eixo Y
        
        // Labels do eixo Y (tempo)
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int numYLabels = 5;
        for (int i = 0; i <= numYLabels; i++) {
            double value = (maxTime / numYLabels) * i;
            int y = chartY + chartHeight - (int)((value / maxTime) * chartHeight);
            g2d.drawLine(chartX - 5, y, chartX, y);
            String label = df.format(value) + "s";
            g2d.drawString(label, chartX - 50, y + 4);
        }
        
        // Label do eixo Y
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("Tempo (segundos)", 5, chartY - 10);
        
        // Desenhar barras
        int barGroupWidth = chartWidth / dataList.size();
        int barWidth = Math.min(barGroupWidth / 4, 40);
        int spacing = 5;
        
        Color dijkstraColor = new Color(255, 99, 71);   // Vermelho
        Color kruskalColor = new Color(60, 179, 113);   // Verde
        Color primColor = new Color(65, 105, 225);      // Azul
        
        for (int i = 0; i < dataList.size(); i++) {
            ChartData data = dataList.get(i);
            int groupX = chartX + (i * barGroupWidth) + barGroupWidth / 6;
            
            // Barra Dijkstra
            int dijkstraHeight = (int)((data.dijkstraTime / maxTime) * chartHeight);
            g2d.setColor(dijkstraColor);
            g2d.fillRect(groupX, chartY + chartHeight - dijkstraHeight, barWidth, dijkstraHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(groupX, chartY + chartHeight - dijkstraHeight, barWidth, dijkstraHeight);
            
            // Barra Kruskal
            int kruskalHeight = (int)((data.kruskalTime / maxTime) * chartHeight);
            g2d.setColor(kruskalColor);
            g2d.fillRect(groupX + barWidth + spacing, chartY + chartHeight - kruskalHeight, barWidth, kruskalHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(groupX + barWidth + spacing, chartY + chartHeight - kruskalHeight, barWidth, kruskalHeight);
            
            // Barra Prim
            int primHeight = (int)((data.primTime / maxTime) * chartHeight);
            g2d.setColor(primColor);
            g2d.fillRect(groupX + 2 * (barWidth + spacing), chartY + chartHeight - primHeight, barWidth, primHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(groupX + 2 * (barWidth + spacing), chartY + chartHeight - primHeight, barWidth, primHeight);
            
            // Label do arquivo (rotacionado se necessário)
            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            g2d.setColor(Color.BLACK);
            String label = data.fileName.length() > 15 ? 
                          data.fileName.substring(0, 12) + "..." : data.fileName;
            int labelX = groupX + barWidth;
            g2d.drawString(label, labelX, chartY + chartHeight + 15);
        }
        
        // Legenda
        int legendY = getHeight() - 35;
        int legendX = chartX;
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        
        g2d.setColor(dijkstraColor);
        g2d.fillRect(legendX, legendY, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, 15, 15);
        g2d.drawString("Dijkstra", legendX + 20, legendY + 12);
        
        legendX += 100;
        g2d.setColor(kruskalColor);
        g2d.fillRect(legendX, legendY, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, 15, 15);
        g2d.drawString("Kruskal", legendX + 20, legendY + 12);
        
        legendX += 100;
        g2d.setColor(primColor);
        g2d.fillRect(legendX, legendY, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, 15, 15);
        g2d.drawString("Prim", legendX + 20, legendY + 12);
    }
    
    private void drawSizeChart(Graphics2D g2d) {
        int margin = 60;
        int chartWidth = getWidth() - 2 * margin;
        int chartHeight = getHeight() - 2 * margin - 60;
        int chartX = margin;
        int chartY = margin;
        
        // Título
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.BLACK);
        String title = "Tamanho dos Grafos (Vértices e Arcos)";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 25);
        
        // Encontrar valores máximos
        int maxValue = 0;
        for (ChartData data : dataList) {
            maxValue = Math.max(maxValue, Math.max(data.vertices, data.arcs));
        }
        
        if (maxValue == 0) maxValue = 1;
        
        // Desenhar eixos
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight);
        g2d.drawLine(chartX, chartY, chartX, chartY + chartHeight);
        
        // Labels do eixo Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int numYLabels = 5;
        for (int i = 0; i <= numYLabels; i++) {
            int value = (maxValue / numYLabels) * i;
            int y = chartY + chartHeight - (int)(((double)value / maxValue) * chartHeight);
            g2d.drawLine(chartX - 5, y, chartX, y);
            g2d.drawString(String.format("%,d", value), chartX - 50, y + 4);
        }
        
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("Quantidade", 5, chartY - 10);
        
        // Desenhar barras
        int barGroupWidth = chartWidth / dataList.size();
        int barWidth = Math.min(barGroupWidth / 3, 50);
        int spacing = 10;
        
        Color verticesColor = new Color(100, 149, 237);  // Azul claro
        Color arcsColor = new Color(255, 165, 0);        // Laranja
        
        for (int i = 0; i < dataList.size(); i++) {
            ChartData data = dataList.get(i);
            int groupX = chartX + (i * barGroupWidth) + barGroupWidth / 4;
            
            // Barra de vértices
            int vHeight = (int)(((double)data.vertices / maxValue) * chartHeight);
            g2d.setColor(verticesColor);
            g2d.fillRect(groupX, chartY + chartHeight - vHeight, barWidth, vHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(groupX, chartY + chartHeight - vHeight, barWidth, vHeight);
            
            // Barra de arcos
            int aHeight = (int)(((double)data.arcs / maxValue) * chartHeight);
            g2d.setColor(arcsColor);
            g2d.fillRect(groupX + barWidth + spacing, chartY + chartHeight - aHeight, barWidth, aHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(groupX + barWidth + spacing, chartY + chartHeight - aHeight, barWidth, aHeight);
            
            // Label
            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            String label = data.fileName.length() > 15 ? 
                          data.fileName.substring(0, 12) + "..." : data.fileName;
            g2d.drawString(label, groupX, chartY + chartHeight + 15);
        }
        
        // Legenda
        int legendY = getHeight() - 35;
        int legendX = chartX + 50;
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        
        g2d.setColor(verticesColor);
        g2d.fillRect(legendX, legendY, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, 15, 15);
        g2d.drawString("Vértices", legendX + 20, legendY + 12);
        
        legendX += 100;
        g2d.setColor(arcsColor);
        g2d.fillRect(legendX, legendY, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, 15, 15);
        g2d.drawString("Arcos", legendX + 20, legendY + 12);
    }
}
