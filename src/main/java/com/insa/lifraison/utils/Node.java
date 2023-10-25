package com.insa.lifraison.utils;

public class Node implements Comparable<Node> {
    private final int index;
    private final double distance;
    public Node(int index, double distance) {
        this.index = index;
        this.distance = distance;
    }

    public int getIndex() {
        return index;
    }

    public double getDistance() {
        return distance;
    }

    public int compareTo(Node node) {
        return Double.compare(this.distance, node.distance);
    }
}