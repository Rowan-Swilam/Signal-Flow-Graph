package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import control.Actions;

public class GUIview extends JFrame {

    private static final long serialVersionUID = 1L;
    public static JButton enter, calculate;
    public static JButton ok, delete;
    public static JTextField from, to, numberOfNodes, gain;
    public JLabel fromLabel, toLabel, numberLabel, gainLabel, okLabel;
    public JLabel hint;
    public static JLabel relations;

    public GUIview() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 700, 400);
        Container con = this.getContentPane();
        con.setBackground(new Color(77, 163, 168));
        setTitle("Signal FLow Graph");
        setLayout(null);
        setResizable(false);

        // buttons
        enter = new JButton("ADD");
        enter.setFont(new Font ("Jokerman", Font.PLAIN, 16));
        enter.setBounds(500, 180, 120, 31);
        Actions obj = new Actions();
        enter.addActionListener(obj);
        enter.setEnabled(false);
        getContentPane().add(enter);

        
        calculate = new JButton("CALCULATE");
        calculate.setFont(new Font ("Jokerman", Font.PLAIN, 16));
        calculate.setBounds(200, 250, 270, 31);
        calculate.setEnabled(false);
        calculate.addActionListener(obj);
        getContentPane().add(calculate);

        ok = new JButton();
        ok.setBounds(380, 70, 30, 31);
        ok.addActionListener(obj);
        ImageIcon img = new ImageIcon(new ImageIcon(this.getClass().getResource("/hands-ok-icon.png")).getImage());
        ok.setIcon(img);
        getContentPane().add(ok);
        
        delete = new JButton("DELETE");
        delete.setFont(new Font ("Jokerman", Font.PLAIN, 16));
        delete.setBounds(500, 250, 120, 31);
        delete.addActionListener(obj);
        delete.setEnabled(false);
        getContentPane().add(delete);
        
        // textFields
        from = new JTextField();
        from.setBounds(50, 180, 120, 30);
        from.setEnabled(false);
        getContentPane().add(from);

        to = new JTextField();
        to.setBounds(200, 180, 120, 30);
        to.setEnabled(false);
        getContentPane().add(to);

        numberOfNodes = new JTextField();
        numberOfNodes.setBounds(260, 70, 120, 30);
        getContentPane().add(numberOfNodes);

        gain = new JTextField();
        gain.setBounds(350, 180, 120, 30);
        gain.setEnabled(false);
        getContentPane().add(gain);

        // Labels
        fromLabel = new JLabel("From");
        fromLabel.setBounds(50, 140, 200, 50);
        fromLabel.setFont(new Font ("Jokerman", Font.BOLD, 16));
        getContentPane().add(fromLabel);

        toLabel = new JLabel("To");
        toLabel.setBounds(200, 140, 200, 50);
        toLabel.setFont(new Font ("Jokerman", Font.BOLD, 16));
        getContentPane().add(toLabel);

        numberLabel = new JLabel("Enter number of nodes: ");
        numberLabel.setFont(new Font ("Jokerman", Font.BOLD, 16));
        numberLabel.setBounds(30, 60, 220, 50);
        getContentPane().add(numberLabel);

        gainLabel = new JLabel("Gain");
        gainLabel.setBounds(350, 140, 150, 50);
        gainLabel.setFont(new Font ("Jokerman", Font.BOLD, 16));
        getContentPane().add(gainLabel);
        
        hint = new JLabel("*HINT: ONLY CAPITAL ALPHABETS ARE ALLOWED FOR NODES");
        hint.setBounds(30, 300, 400, 50);
        hint.setFont(new Font ("Jokerman", Font.BOLD, 10));
        getContentPane().add(hint);
        
        relations = new JLabel("Relations: ");
        relations.setBounds(450,0,200,200);
        relations.setFont(new Font ("Jokerman", Font.PLAIN, 12));
        getContentPane().add(relations);
        
        

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        GUIview v = new GUIview();
        v.setVisible(true);
    }

}
