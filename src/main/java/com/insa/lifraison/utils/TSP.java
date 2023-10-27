package com.insa.lifraison.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TSP {
    private Integer[] bestSol;
    protected Graph g;
    private Double bestSolCost;
    private int timeLimit;
    private long startTime;

    public void searchSolution(int timeLimit, Graph g){
        if (timeLimit <= 0) return;
        startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.g = g;
        bestSol = new Integer[g.getNbVertices()];
        Collection<Integer> unvisited = new ArrayList<>(g.getNbVertices()-1);
        for (int i=1; i<g.getNbVertices(); i++) unvisited.add(i);
        Collection<Integer> visited = new ArrayList<>(g.getNbVertices());
        visited.add(0); // The first visited vertex is 0
        bestSolCost = Double.MAX_VALUE;
        branchAndBound(0, unvisited, visited, 0);
    }

    public Integer getSolution(int i){
        if (g != null && i>=0 && i<g.getNbVertices())
            return bestSol[i];
        return -1;
    }

    public double getSolutionCost(){
        if (g != null)
            return bestSolCost;
        return -1;
    }

    /**
     * @param currentVertex the vertex currently being visited
     * @param unvisited the set of nodes that have not been visited
     * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting
     * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
     */
    protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
        return 0;
    }

    /**
     * @param currentVertex the vertex currently being visited
     * @param unvisited the set of nodes that have not been visited
     * @param g the graph for which the iterator will be created
     * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
     */
    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

    /**
     * Method of a branch and bound algorithm for solving the TSP in <code>g</code>.
     * @param currentVertex the last visited vertex
     * @param unvisited the set of vertex that have not yet been visited
     * @param visited the sequence of vertices that have been already visited (including currentVertex)
     * @param currentCost the cost of the path corresponding to <code>visited</code>
     */
    private void branchAndBound(int currentVertex, Collection<Integer> unvisited,
                                Collection<Integer> visited, double currentCost){
        if (System.currentTimeMillis() - startTime > timeLimit) return;
        if (unvisited.isEmpty()){
            if (g.isArc(currentVertex,0)){
                if (currentCost+g.getDistance(currentVertex,0) < bestSolCost){
                    visited.toArray(bestSol);
                    bestSolCost = currentCost+g.getDistance(currentVertex,0);
                }
            }
        } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
            Iterator<Integer> it = iterator(currentVertex, unvisited, g);
            while (it.hasNext()){
                Integer nextVertex = it.next();
                visited.add(nextVertex);
                unvisited.remove(nextVertex);
                branchAndBound(nextVertex, unvisited, visited,
                        currentCost+g.getDistance(currentVertex, nextVertex));
                visited.remove(nextVertex);
                unvisited.add(nextVertex);
            }
        }
    }

}