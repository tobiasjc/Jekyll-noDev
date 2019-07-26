/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caminhominimo;

/**
 *
 * @author jose
 */
public class DistancePriorityQueue {

    private final double[] d;
    private final int[] pq;
    private int n = 0;

    public DistancePriorityQueue(double[] d) {
        this.d = d;
        pq = new int[d.length + 1];
        for (int i = 1; i <= d.length; i++) {
            insert(i - 1);
        }
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        int j;
        while (2 * k <= n) {
            j = 2 * k;
            if (j < n && greater(j, j + 1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }
    }

    final void insert(int v) {
        pq[++n] = v;
        swim(n);
    }

    final int remove() {
        int min = pq[1];
        exch(1, n--);
        sink(1);
        return min;
    }

    private boolean greater(int u, int v) {
        return d[pq[u]] > d[pq[v]];
    }

    private void exch(int i, int j) {
        int t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }

    public boolean empty() {
        return n == 0;
    }

}
