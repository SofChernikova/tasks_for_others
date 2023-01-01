package com.company.PartOfTree;

public class MapNode {  // узел нашего дерева
    private MapEntry mapEntry;
    private MapNode leftNode;
    private MapNode rightNode;
    private int height;


    public MapNode(MapEntry mapEntry) {
        this.mapEntry = mapEntry;
        this.leftNode = null;
        this.rightNode = null;
        this.height = 0;
    }

    public MapEntry getMapEntry() {
        return mapEntry;
    }

    public void setMapEntry(MapEntry mapEntry) {

        this.mapEntry = mapEntry;
    }


    public MapNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(MapNode leftNode) {
        this.leftNode = leftNode;
    }

    public MapNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(MapNode rightNode) {
        this.rightNode = rightNode;
    }


    public void recalcHeight() {
        height = Math.max((leftNode == null ? -1 : leftNode.height), (rightNode == null ? -1 : rightNode.height)) + 1;
    }

    public int getHeightDiff() {
        return (leftNode == null ? -1 : leftNode.height) - (rightNode == null ? -1 : rightNode.height);
    }

    public boolean printRange(){
        System.out.println(getMapEntry().getKey().getLeft() + "..." + getMapEntry().getKey().getRight());
        return false;
    }

    public boolean printNode(){
        System.out.println("Key: " + getMapEntry().getKey().getLeft() + "..." + getMapEntry().getKey().getRight());
        System.out.println("Value: " + getMapEntry().getValue());
        return false;
    }
}
