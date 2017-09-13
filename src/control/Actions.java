package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import model.SFGmodel;
import view.*;

public class Actions implements ActionListener {

    private int numberOfNodes;
    private boolean[][] arrayOfRelations;
    private double[][] arrayOfGains;
    private GUI guiObject;

    public Actions() {
        guiObject = new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == GUIview.ok) {
            if (!GUIview.numberOfNodes.getText().isEmpty()) {
                numberOfNodes = Integer.parseInt(GUIview.numberOfNodes
                        .getText());
                arrayOfRelations = new boolean[numberOfNodes + 1][numberOfNodes
                        + 1];
                arrayOfGains = new double[numberOfNodes + 1][numberOfNodes + 1];
                GUIview.numberOfNodes.setEnabled(false);
                GUIview.ok.setEnabled(false);
                GUIview.enter.setEnabled(true);
                GUIview.from.setEnabled(true);
                GUIview.to.setEnabled(true);
                GUIview.gain.setEnabled(true);

            }

        } else if (e.getSource() == GUIview.enter) {
            if (!GUIview.from.getText().isEmpty() && !GUIview.to.getText()
                    .isEmpty() && !GUIview.gain.getText().isEmpty()) {
                int x = GUIview.from.getText().charAt(0) - '@';
                int y = GUIview.to.getText().charAt(0) - '@';
                if ((x < 1 || x > numberOfNodes) || (y < 1
                        || y > numberOfNodes)) {
                    JOptionPane.showMessageDialog(null,
                            "Enter an ALPHABET between A and " + Character
                                    .toString((char) (numberOfNodes + '@')),
                            "ERROR: ", JOptionPane.INFORMATION_MESSAGE);
                    GUIview.from.setText("");
                    GUIview.to.setText("");
                    GUIview.gain.setText("");
                } else if (y == 1) {
                    JOptionPane.showMessageDialog(null,
                            "The first node is a SOURCE only",
                            "ERROR: ", JOptionPane.INFORMATION_MESSAGE);
                    GUIview.from.setText("");
                    GUIview.to.setText("");
                    GUIview.gain.setText("");

                } else {
                    arrayOfRelations[x][y] = true;
                    arrayOfGains[x][y] = Double.parseDouble(GUIview.gain
                            .getText());
                    String last = "Relations: ";
                    String s = "";
                    if (GUIview.relations
                            .getText() != "Relations: ") {
                        last = GUIview.relations
                                .getText().substring(12, GUIview.relations
                                        .getText().length() - 14);
                        s = "<html><body>" + last + " , " + GUIview.from
                                .getText()
                                .charAt(0) + "-->" + GUIview.to.getText()
                                        .charAt(0)
                                + "</body></html>";

                    } else {

                        s = "<html><body>" + last + "</br>" + GUIview.from
                                .getText()
                                .charAt(0) + "-->" + GUIview.to.getText()
                                        .charAt(0)
                                + "</body></html>";
                    }
                    GUIview.relations.setText(s);
                    GUIview.from.setText("");
                    GUIview.to.setText("");
                    GUIview.gain.setText("");
                    GUIview.calculate.setEnabled(true);
                    GUIview.delete.setEnabled(true);
                }
            }

        } else if (e.getSource() == GUIview.calculate) {
            SFGmodel obj = new SFGmodel(arrayOfRelations, arrayOfGains);
            obj.run();
            guiObject.initialize();
            guiObject.buildGraph(arrayOfRelations, arrayOfGains);
        } else if (e.getSource() == GUIview.delete) {
            if (!GUIview.from.getText().isEmpty() && !GUIview.to.getText()
                    .isEmpty()) {
                int x = GUIview.from.getText().charAt(0) - '@';
                int y = GUIview.to.getText().charAt(0) - '@';
                if (arrayOfRelations[x][y]) {
                    arrayOfRelations[x][y] = false;
                    arrayOfGains[x][y] = 0;
                    String s = GUIview.relations.getText();
                    s = s.replaceAll(GUIview.from.getText().charAt(0) + "-->"
                            + GUIview.to.getText().charAt(0)+" ,", "");
                    GUIview.relations.setText(s);  
                    GUIview.from.setText("");
                    GUIview.to.setText("");
                    
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Nodes not found", "ERROR: ",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            }

        }

    }

}
