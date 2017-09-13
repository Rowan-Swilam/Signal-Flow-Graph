package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.GUI;

public class SFGmodel {

    // to be defined when number of nodes are known.
    private boolean[][] sfgRelations;
    private boolean[] visited;
    private double[][] gains;
    private HashMap<String, Double> forwardPaths;
    private ArrayList<Double> deltas;
    private HashMap<String, Double> loops;
    private ArrayList<ArrayList<String>> nonTouchingLoops;
    private ArrayList<ArrayList<String>> nonTouchingLoopsWithForwardPath;
    private static GUI obj;

    private ArrayList<String> a;

    public SFGmodel(boolean[][] sfgRelations, double[][] gains) {
        this.sfgRelations = sfgRelations;
        this.gains = gains;
        visited = new boolean[sfgRelations.length];
        forwardPaths = new HashMap<>();
        deltas = new ArrayList<>();
        loops = new HashMap<>();
        a = new ArrayList<>();
        nonTouchingLoops = new ArrayList<>();
        nonTouchingLoopsWithForwardPath = new ArrayList<>();

    }

    // first node is already concatenated;
    private void pathDetector(int start, int end, String s, double gain) {
        visited[start] = true;
        for (int j = start; j <= end; j++) {
            if (sfgRelations[start][j] && !visited[j]) {
                pathDetector(j, end, s + Character.toString((char)(j+'@')), gain
                        * gains[start][j]);
                visited[j] = false;
            }
        }
        
        if (s.charAt(s.length() - 1) == Character.toString((char)(end+'@')).charAt(0)) {
            forwardPaths.put(s, gain);
        }
    }

    private void bfs(int node) {
        Queue<String> qu = new LinkedList<>();
        Queue<Double> quGain = new LinkedList<>();
        qu.add(Character.toString((char)(node+'@')));
        quGain.add(1.0);
        while (!qu.isEmpty()) {
            String parent = qu.poll();
            Double parentGain = quGain.poll();
            if (parent.length() > 1 && parent.charAt(0) == parent.charAt(parent
                    .length() - 1)) {
                if (!exists(parent)) {
                    loops.put(parent, parentGain);
                }
                continue;
            }
            for (int i = 1; i < sfgRelations.length; i++) {
                if (sfgRelations[parent.charAt(parent.length() - 1)-'@'][i]) {
                    String child = parent + Character.toString((char)(i+'@'));
                    Double childGain = parentGain * gains[parent.charAt(parent.length() - 1)-'@'][i];
                    if (!parent.contains(Character.toString((char)(i+'@'))) || parent.charAt(
                            0) == Character.toString((char)(i+'@')).charAt(0)) {
                        qu.add(child);
                        quGain.add(childGain);
                    }
                }
            }

        }

    }

    private boolean exists(String s) {
        Set<Character> setOfs = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            setOfs.add(s.charAt(i));
        }
        s = setOfs.toString();
        if (!a.isEmpty()) {
            if (a.contains(s)) {
                return true;
            }
        }
        a.add(s);
        return false;
    }

    private void loopsDetector() {
        String s;
        for (int i = 1; i < sfgRelations.length; i++) {
            if (sfgRelations[i][i]) {
                s = Character.toString((char)(i+'@')) + Character.toString((char)(i+'@'));
                loops.put(s, gains[i][i]);
            }
            bfs(i);
        }

    }

    private void nonTouchingLoopsDetector() {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> l;
        Object[] keys = loops.keySet().toArray();
        for (int i = 0; i < (1 << loops.size()); i++) {
            for (int j = 0; j < loops.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    a.add((String) keys[j]);
                }
            }
            if (a.size() > 1 && !isTouching(a)) {
                l = new ArrayList<>();
                for (int j = 0; j < a.size(); j++) {
                    l.add(a.get(j));
                }
                nonTouchingLoops.add(l);
            }
            a = new ArrayList<>();
        }
    }

    private boolean isTouching(ArrayList<String> toBeTested) {
        for (int i = 0; i < toBeTested.size() - 1; i++) {
            for (int j = i + 1; j < toBeTested.size(); j++) {
                if (testing(toBeTested.get(i), toBeTested.get(j)))
                    return true;
            }
        }
        return false;
    }

    private boolean testing(String loop1, String loop2) {
        for (int i = 0; i < loop1.length(); i++) {
            for (int j = 0; j < loop2.length(); j++) {
                if (loop1.charAt(i) == loop2.charAt(j))
                    return true;
            }
        }
        return false;
    }

    private void nonTouchingPathDetector(String f) {
        Object[] loopKeys = loops.keySet().toArray();
        ArrayList<String> a;
        nonTouchingLoopsWithForwardPath = new ArrayList<>();

        for (int j = 0; j < loops.size(); j++) {
            if (!testing(f, (String) loopKeys[j])) {
                a = new ArrayList<>();
                a.add((String) loopKeys[j]);
                nonTouchingLoopsWithForwardPath.add(a);
            }
        }

        for (int j = 0; j < nonTouchingLoops.size(); j++) {
            int k;
            for (k = 0; k < nonTouchingLoops.get(j).size(); k++) {
                if (testing(f, nonTouchingLoops.get(j).get(
                        k))) {
                    break;
                }
            }
            if (k == nonTouchingLoops.get(j).size()) {
                nonTouchingLoopsWithForwardPath.add(nonTouchingLoops.get(
                        j));
            }

        }
    }

    private void calculatingDeltas() {
        Object[] keys = forwardPaths.keySet().toArray();
        for (int i = 0; i < forwardPaths.size(); i++) {
            int maxLoops = 0;
            double delta = 1;
            nonTouchingPathDetector((String) keys[i]);
            for (int z = 0; z < nonTouchingLoopsWithForwardPath.size(); z++) {
                if (nonTouchingLoopsWithForwardPath.get(z).size() > maxLoops)
                    maxLoops = nonTouchingLoopsWithForwardPath.get(z).size();
            }
            double ans = 0;
            for (int k = 1; k <= maxLoops; k++) {
                ans = 0;
                for (int j = 0; j < nonTouchingLoopsWithForwardPath
                        .size(); j++) {
                    if (nonTouchingLoopsWithForwardPath.get(j).size() == k) {
                        double g1 = loops.get(nonTouchingLoopsWithForwardPath
                                .get(j).get(0));
                        for (int l = 1; l < nonTouchingLoopsWithForwardPath.get(
                                j).size(); l++) {
                            double g = loops.get(nonTouchingLoopsWithForwardPath
                                    .get(j).get(l));
                            g1 *= g;
                        }
                        ans += g1;
                    }
                }
                ans *= Math.pow(-1, k);
                delta += ans;
            }
            deltas.add(Math.round(delta * 100.0) / 100.0);
        }
    }

    private double calculatingDelta() {
        double ans = 0;
        double delta = 1;
        Object[] keys = loops.keySet().toArray();
        for (int i = 0; i < loops.size(); i++) {
            ans += loops.get((String) keys[i]);

        }
        delta -= ans;
        ans = 0;
        int max = 0;
        for (int i = 0; i < nonTouchingLoops.size(); i++) {
            if (nonTouchingLoops.get(i).size() > max) {
                max = nonTouchingLoops.get(i).size();
            }
        }

        for (int j = 2; j <= max; j++) {
            ans = 0;
            for (int i = 0; i < nonTouchingLoops.size(); i++) {
                if (nonTouchingLoops.get(i).size() == j) {
                    double g = 1;
                    for (int k = 0; k < j; k++) {
                        g *= loops.get(nonTouchingLoops.get(i).get(k));
                    }
                    ans += g;
                }
            }
            ans *= Math.pow(-1, j);
            delta += ans;
        }
        return delta;
    }

    private double overallGain() {
        double tG = 0;
        Object[] forward = forwardPaths.keySet().toArray();
        for (int i = 0; i < forwardPaths.size(); i++) {
            tG += (forwardPaths.get(forward[i]) * deltas.get(i));
        }
        tG /= calculatingDelta();
        return Math.round(tG * 100.0) / 100.0;
    }

    public void run() {
        pathDetector(1, sfgRelations.length-1, "A", 1);
        if(forwardPaths.isEmpty()) {
            JOptionPane.showMessageDialog(null,"There MUST be at least ONE forward path",
                    "ERROR: ", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        loopsDetector();
        nonTouchingLoopsDetector();
        calculatingDeltas();
        obj = new GUI(forwardPaths, loops, nonTouchingLoops, deltas, overallGain());
        obj.createFrame();
    }

}
