/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caminhominimo;

import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author jose
 */
public class Topologic {

    private static final Stack<Integer> T = new Stack<>();
    private static int[] d;
    private static int[] f;
    private static boolean[] s;
    private static final Stack<Integer> S = new Stack<>();

    public Topologic(AbstractGraph G) {
        DFS(G);
    }

    public static Stack<Integer> DFS(AbstractGraph G) {
        d = new int[G.V()];
        f = new int[d.length];
        s = new boolean[f.length];
        Stack<Integer> temp = new Stack<>();

        Arrays.fill(s, false);
        int time = 0;

        for (int i = 0; i < G.V(); i++) {
            if (!s[i]) {
                S.push(i);
                time = time + 1;
                d[i] = time;
                s[i] = true;

                for (Edge e : G.listAdj(i)) {
                    int u = e.one();
                    int v = e.other(u);
                    if (!s[v]) {
                        temp.push(v);
                    }
                }
                while (!temp.isEmpty()) {
                    S.push(temp.pop());
                }
            }

            while (!S.empty()) {
                if (!s[S.peek()]) {
                    time = time + 1;
                    d[S.peek()] = time;
                    s[S.peek()] = true;

                    for (Edge e : G.listAdj(S.peek())) {
                        int u = e.one();
                        int v = e.other(u);
                        if (!s[v]) {
                            temp.push(v);
                        }
                    }

                    while (!temp.isEmpty()) {
                        S.push(temp.pop());
                    }
                } else {
                    if (f[S.peek()] == 0) {
                        T.push(S.peek());
                        time = time + 1;
                        f[S.pop()] = time;
                    } else {
                        S.pop();
                    }
                }
            }
        }
        return T;
    }
}
