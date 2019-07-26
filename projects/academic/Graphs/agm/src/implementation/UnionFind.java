/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

/**
 *
 * @author jose
 */
public class UnionFind {

    private int count;
    private final int[] id;
    private final int[] s;

    public UnionFind(int n) {
        this.count = n;
        id = new int[n];
        s = new int[n];

        for (int i = 0; i < n; i++) {
            s[i] = 1;
            id[i] = i;
        }
    }

    public int find(int u) {
        while (u != id[u]) {
            u = id[u];
        }
        return u;
    }

    public boolean connected(int u, int v) {
        return (find(u) == find(v));
    }

    public void union(int u, int v) {
        int x = find(u);
        int z = find(v);

        if (x != z) {
            if (s[x] < s[z]) {
                id[x] = z;
                s[z] += s[x];
            } else {
                id[z] = x;
                s[x] += s[z];
            }
            count--;
        }
    }

    public int count() {
        return count;
    }
}
