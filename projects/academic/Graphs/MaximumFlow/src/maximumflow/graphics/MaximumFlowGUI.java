/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maximumflow.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import maximumflow.AbstractFlowNetwork;
import maximumflow.FlowEdge;

/**
 *
 * @author jose
 */
public class MaximumFlowGUI extends JFrame {

    private final FlowEdge[] path;
    private final AbstractFlowNetwork G;
    private final String s;
    private final String t;
    private final double maximumFlow;

    private final int WIDTH;
    private final int HEIGHT;

    class DrawGraphPanel extends JPanel {

        public DrawGraphPanel() {
            this.setOpaque(true);
            this.setBackground(Color.white);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            double margin = 20;
            // initial settings for graphics 
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int sW = super.getWidth();
            int sH = super.getHeight();
            Shape n;

            /* draw full flow network below */
            double r = Math.min(sW, sH) / 2 - margin;
            double k = 0;
            double a = sW / 2;
            double b = sH / 2;
            double er = 8;

            Point2D[] positions = new Point2D[G.V()];

            double x;
            double y;

            for (int i = 0; i < G.V(); i++) {
                k = 2 * Math.PI * i / G.V();
                x = a + r * Math.cos(k);
                y = b + r * Math.sin(k);
                positions[i] = new Point2D.Double(x, y);
                n = new Ellipse2D.Double(x - er, y - er, er, er);
                g2.setColor(Color.black);
                g2.drawString(String.valueOf(i), (float) (x - er), (float) (y - er));
                g2.setColor(Color.red);
                g2.draw(n);
                g2.fill(n);
            }

            // draw all edges
            g2.setColor(Color.lightGray);
            for (FlowEdge e : G.edges()) {
                int u = e.one();
                int v = e.other(u);
                n = new Line2D.Double(positions[u].getX() - er / 2,
                        positions[u].getY() - er / 2,
                        positions[v].getX() - er / 2,
                        positions[v].getY() - er / 2
                );
                g2.draw(n);
            }

            // draw augmenting path edges
            for (FlowEdge e : path) {
                if (e != null && e.getFlow() > 0) {
                    int u = e.one();
                    int v = e.other(u);
                    n = new Line2D.Double(positions[u].getX() - er / 2,
                            positions[u].getY() - er / 2,
                            positions[v].getX() - er / 2,
                            positions[v].getY() - er / 2
                    );
                    g2.setColor(Color.darkGray);
                    g2.drawString(String.valueOf(e.getFlow()) + "/" + e.getCapacity(),
                            (int) (positions[u].getX() + (positions[v].getX() - positions[u].getX()) / 2),
                            (int) (positions[u].getY() + (positions[v].getY() - positions[u].getY()) / 2));
                    g2.setColor(Color.blue);
                    g2.draw(n);
                }
            }
        }
    }

    class MaximumFlowPanel extends JPanel {

        public MaximumFlowPanel() {
            this.setOpaque(true);
            this.setPreferredSize(new Dimension(super.getWidth(), 25));
            this.setBackground(new Color(239, 237, 238));
        }

        @Override
        public void paintComponent(Graphics g) {
            // initial settings for graphics
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.RED);

            g2.drawString("Maximum flow = " + String.valueOf(maximumFlow), 100, 20);
        }
    }

    public MaximumFlowGUI(FlowEdge[] path, AbstractFlowNetwork G, String s, String t, double maximumFlow) {
        this.WIDTH = 800;
        this.HEIGHT = 600;
        this.path = path;
        this.G = G;
        this.s = s;
        this.t = t;
        this.maximumFlow = maximumFlow;
        // create a content JPanel only to show the maximum flow
        JPanel flowPanel = new MaximumFlowPanel();

        // create our JPanel for the graph
        JPanel graphPanel = new DrawGraphPanel();

        // setting JFrame and adding panel to it
        this.setSize(this.WIDTH, this.HEIGHT);
        this.setTitle("Maximum Flow Network");
        this.setLayout(new BorderLayout());
        this.add(flowPanel, BorderLayout.PAGE_START);
        this.add(graphPanel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
    }
}
