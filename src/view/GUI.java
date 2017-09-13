package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

public class GUI {

    protected static String styleSheet =
            "node {" + "   fill-color: #4da3a8;" + "   size: 40px, 40px;" + "   text-size: 16%;" + "}"
                    + "edge {" + "   arrow-shape: arrow;"
                    + "   arrow-size: 20px, 4px;" + "  text-size: 16%;" + "}";

    public static Graph graph;
    private HashMap<String, Double> forwardPaths;
    private HashMap<String, Double> loops;
    private ArrayList<Double> deltas;
    private ArrayList<ArrayList<String>> nonTouchingLoops;
    private double overallGain;
    private double[][] gains;

    public GUI() {

    }

    public GUI(HashMap<String, Double> forwardPaths,
            HashMap<String, Double> loops,
            ArrayList<ArrayList<String>> nonTouchingLoops,
            ArrayList<Double> deltas, double overallGain) {

        this.forwardPaths = forwardPaths;
        this.loops = loops;
        this.nonTouchingLoops = nonTouchingLoops;
        this.deltas = deltas;
        this.overallGain = overallGain;
    }

    public void initialize() {
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new MultiGraph("Signal Flow Graph");
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);

    }

    public void buildGraph(boolean[][] relations, double[][] gains) {
        this.gains = gains;
        for (int i = 0; i < relations.length; i++) {
            for (int j = 0; j < relations.length; j++) {
                if (relations[i][j]) {
                    graph.addEdge(Character.toString((char)(i+'@')) + Character.toString((char)(j+'@')),
                            Character.toString((char)(i+'@')), Character.toString((char)(j+'@')), true);
                }
            }
        }
        setLabels();
    }

    public void setLabels() {
        for (Node node : graph) {
            node.addAttribute("ui.label",node.getId());
        }
        for (Edge edge : graph.getEdgeSet()) {
            int row = edge.getId().charAt(0)-'@';
            int column = edge.getId().charAt(1)-'@';
            double g = gains[row][column];
            edge.addAttribute("ui.label", Double.toString(g));
        }

    }

    public class MyFrame extends JFrame {
        private static final long serialVersionUID = 8394236698316485656L;

        private Viewer viewer = new Viewer(graph,
                Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        private JPanel view = viewer.addDefaultView(false);

        public MyFrame() {

            view.setPreferredSize(new Dimension(500, 500));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            add((Component) view, BorderLayout.CENTER);
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(500, 300));
            panel.setBackground(new Color(77, 163, 168));
            JLabel label = new JLabel();
            panel.add(label);
            add(panel, BorderLayout.WEST);
            JLabel Result = new JLabel("",SwingConstants.LEFT);
            Result.setText("<html><div style='text-align: center;'>" +"<b>" +"Forward Paths: " +"</b>"+ forwardPaths
                    .toString() + "<br/>" +"<b>" +"Loops: " + "</b>"+ loops.toString()
                    + "<br/>" +"<b>" +"NonTouching Loops: "+"</b>" + nonTouchingLoops
                            .toString() + "<br/>" + "<b>"+"Deltas: "+"</b>" + deltas
                                    .toString() + "<br/>" +"<b>" +"Overall TF: " + "</b>"
                    + overallGain + "</div></html>");
            Result.setFont(new Font ("Verdana", Font.PLAIN, 16));
            panel.setLayout(null);
            Result.setBounds(20, 0, 450, 500);
            panel.add(Result);
            pack();
            setVisible(true);
            setResizable(true);
            viewer.enableAutoLayout();
        }
    }

    public void createFrame() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyFrame();
            }

        });
    }
}
