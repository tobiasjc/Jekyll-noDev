/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caminhominimo;

import java.util.List;

/**
 *
 * @author jose
 * @param <Key>
 */
public class LowPriorityQueue<Key extends Comparable<Key>> {

    private Key[] pq;
    private int n = 0;

    LowPriorityQueue(List<Key> a) {
        pq = (Key[]) new Comparable[a.size() + 1];
        for (Key e : a) {
            insert(e);
        }
    }

    LowPriorityQueue(int max) {
        pq = (Key[]) new Comparable[max + 1];
    }

    final void insert(Key v) {
        pq[++n] = v;
        swim(n);
    }

    final Key remove() {
        Key min = pq[1];
        exch(1, n--);
        sink(1);
        pq[n + 1] = null;
        return min;
    }

    public final int removeIndex() {
        return 1;
    }

    public final Key min() {
        return (pq[1]);
    }

    public boolean empty() {
        return n == 0;
    }

    private boolean greater(int u, int v) {
        return pq[u].compareTo(pq[v]) > 0;
    }

    private void exch(int i, int j) {
        Key t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
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

    public int size() {
        return n;
    }
}
