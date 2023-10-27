package com.insa.lifraison.utils;

import java.util.ArrayList;

public class Graph {
    private final ArrayList<ArrayList<Double>> distances;
    private final int nbVertices;

    public Graph(ArrayList<ArrayList<Double>> distances) {
        this.distances = distances;
        nbVertices = distances.size();
    }

    public int getNbVertices() {
        return nbVertices;
    }

    public double getDistance(int i, int j) {
        if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
            return -1;
        return distances.get(i)
                .get(j);
    }

    public boolean isArc(int i, int j) {
        if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
            return false;
        return i != j;
    }
}
