package com.company.PartOfTree;

public class Range implements Comparable<Range> {   //отрезок
    private int left;
    private int right;

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public Range(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    @Override
    public int compareTo(Range o) {
       return this.left - o.left;
//        если разность больше 0, то this лежит правее о
//        если меньше 0, то this лежит левее о
    }
}
