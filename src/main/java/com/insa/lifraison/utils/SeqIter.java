package com.insa.lifraison.utils;

import java.util.Collection;
import java.util.Iterator;

public class SeqIter implements Iterator<Integer> {
    private Integer[] candidates;
    private int nbCandidates;

    /**
     * Create an iterator to traverse the set of vertices in <code>unvisited</code>
     * which are successors of <code>currentVertex</code> in <code>g</code>
     * Vertices are traversed in the same order as in <code>unvisited</code>
     * @param unvisited
     * @param currentVertex
     * @param g
     */
    public SeqIter(Collection<Integer> unvisited, int currentVertex, Graph g){
        this.candidates = new Integer[unvisited.size()];
        for (Integer s : unvisited){
            if (g.isArc(currentVertex, s))
                candidates[nbCandidates++] = s;
        }
    }

    public boolean hasNext() {
        return nbCandidates > 0;
    }

    public Integer next() {
        nbCandidates--;
        return candidates[nbCandidates];
    }

    public void remove() {}

}
