/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author jose
 */
public class GraphGUI extends JFrame {

    public double[][] p;
    public List<Edge> e;
    public Queue<Edge> m;
    public boolean weights = false;

    public void saveImg(File file) {
        BufferedImage bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        super.getContentPane().getComponent(2).paintAll(g);
        g.dispose();
        try {
            ImageIO.write(bi, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    class GraphPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            double mg = 15.5;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double sW = super.getWidth() - mg;
            double sH = super.getHeight() - mg;
            double es = 5;
            double lt = es / 2;

            Shape n;
            for (double[] l : p) {
                n = new Ellipse2D.Double(l[0] / 100 * sW - lt, l[1] / 100 * sH - lt, es, es);
                g2.setColor(Color.BLUE);
                g2.fill(n);
                g2.draw(n);
            }

//            for (Edge e : e) {
//                int u = e.one();
//                int v = e.other(u);
//                Point2D pu = new Point2D.Double(p[u][0] / 100 * sW, p[u][1] / 100 * sH);
//                Point2D pv = new Point2D.Double(p[v][0] / 100 * sW, p[v][1] / 100 * sH);
//                n = new Line2D.Double(pu, pv);
//                g.setColor(Color.LIGHT_GRAY);
//                g2.fill(n);
//                g2.draw(n);
//            }
            for (Edge e : m) {
                int u = e.one();
                int v = e.other(u);
                double weight = e.weight();

                g2.setFont(new Font("Arial", Font.PLAIN, 8));

                Point2D pu = new Point2D.Double(p[u][0] / 100 * sW, p[u][1] / 100 * sH);
                Point2D pv = new Point2D.Double(p[v][0] / 100 * sW, p[v][1] / 100 * sH);
                g2.setColor(Color.RED);
                n = new Line2D.Double(pu, pv);
                g2.fill(n);
                g2.draw(n);

                g2.setColor(Color.BLUE);
                g2.drawString(Integer.toString(u), (float) (p[u][0] / 100 * sW - 3), (float) (p[u][1] / 100 * sH));
                g2.drawString(Integer.toString(v), (float) (p[v][0] / 100 * sW - 3), (float) (p[v][1] / 100 * sH));

                if (weights == true) {
                    g2.setColor(Color.DARK_GRAY);
                    DecimalFormat df = new DecimalFormat("#.##");
                    double minX = Math.min(p[u][0], p[v][0]);
                    double minY = Math.min(p[u][1], p[v][1]);
                    double difX = Math.abs(p[u][0] - p[v][0]) / 2;
                    double difY = Math.abs(p[u][1] - p[v][1]) / 2;
                    float tw = g2.getFontMetrics().stringWidth("~" + df.format(minX + difX));
                    float pX = (float) ((minX + difX) / 100 * sW - (tw / 2));
                    float pY = (float) ((minY + difY) / 100 * sH);
                    g2.drawString("~" + (df.format(weight)), pX, pY);
                }
            }
            double sum = 0;
            for (Edge e : m) {
                sum += e.weight();
            }
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            int tw = g2.getFontMetrics().stringWidth("total cost = " + Double.toString(sum));
            g2.drawString("total cost = " + Double.toString(sum), (float) (sW + mg - tw), (float) (sH + mg));
        }
    }

    public GraphGUI(double[][] v, List<Edge> e, Queue<Edge> m, String op) {
        this.p = v;
        this.e = e;
        this.m = m;
        this.setTitle("Graph Visualization");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(GraphGUI.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.getContentPane().add(new JButton(new AbstractAction("Save Image") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImg(new File(op.split("\\.(?=[^\\.]+$)")[0] + ".png"));
            }
        }), BorderLayout.EAST);

        this.getContentPane().add(new JRadioButton(new AbstractAction("Show Weights") {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                weights = !(weights);
            }
        }), BorderLayout.PAGE_START);
        this.getContentPane().add(new GraphPanel(), BorderLayout.CENTER);
        this.setVisible(true);
    }
}
