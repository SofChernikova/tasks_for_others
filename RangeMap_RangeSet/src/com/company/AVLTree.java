package com.company;

import com.company.PartOfTree.MapEntry;
import com.company.PartOfTree.MapNode;
import com.company.PartOfTree.Range;

public class AVLTree {
    private MapNode root = null;
    private int size = 0;

    public MapNode getRoot() {
        return root;
    }

    public void put(MapEntry value) {
        // подумать, какой корень класть
//        MapEntry oldValue = null;
//        if(TreeAlgorithms.getNode(root, value).getMapEntry() != null){
//            oldValue = TreeAlgorithms.getNode(root, value).getMapEntry();
//        }
        this.root = put(root, value);
//        return oldValue;
    }

    public void remove(int value) {
        this.root = remove(root, value);
    }

    public void clear() {
        root = null;
        size = 0;
    }

    //-------------------------------------------------------------------
    private MapNode put(MapNode node, MapEntry entry) {
        if (node == null) {
            size++;
            return new MapNode(entry);
        }
        int cmp = entry.compareTo(node.getMapEntry());
        if (cmp == 0) {
            // в узле значение, равное value
            node.setMapEntry(entry);
        } else {
            if (cmp < 0) {
                node.setLeftNode(put(node.getLeftNode(), entry));
            } else {
                node.setRightNode(put(node.getRightNode(), entry));
            }
            node.recalcHeight();
            // балансировка
            node = balance(node);
        }
        return node;
    }

    private MapNode remove(MapNode node, int value) {
        if (node == null) {
            System.out.println("Упс, отрезка не существует!");
            return null;
        }
        // int cmp = value.compareTo(node.getMapEntry());
        if (value >= node.getMapEntry().getKey().getLeft() && value <= node.getMapEntry().getKey().getRight()) {
//            System.out.println("Удаленный отрезок: " + node.getMapEntry().getKey().getLeft() + ".." + node.getMapEntry().getKey().getRight() + ":");
            // в узле значение, равное value
            if (node.getLeftNode() != null && node.getRightNode() != null) {
                System.out.println("Удаленный отрезок: " + node.getMapEntry().getKey().getLeft() + ".." + node.getMapEntry().getKey().getRight() + ":");
                node.setMapEntry(TreeAlgorithms.getMinNode(node.getRightNode()).getMapEntry());
                node.setRightNode(remove(node.getRightNode(), node.getMapEntry().getKey().getLeft()));
            } else {

                node = (node.getLeftNode() != null) ? node.getLeftNode() : node.getRightNode();
                size--;
            }
        } else if (value < node.getMapEntry().getKey().getLeft()) {
            node.setLeftNode(remove(node.getLeftNode(), value));
            node.recalcHeight();
        } else {
            node.setRightNode(remove(node.getRightNode(), value));
            node.recalcHeight();
        }
        return balance(node);
    }


    //-------------------------------------------------------------------

    private MapNode balance(MapNode node) {
        if (node == null) {
            return null;
        }
        if (node.getHeightDiff() < -1) {
            // высота правого поддерева для node больше левого более, чем на 1 (на 2)

            if (node.getRightNode() != null && node.getRightNode().getHeightDiff() > 0) {
                // двойной право-левый поворот (RL-rotation)
                node.setRightNode(rightRotate(node.getRightNode()));
                node = leftRotate(node);
            } else {
                // левый поворот (L-rotation)
                node = leftRotate(node);
            }
        } else if (node.getHeightDiff() > 1) {
            // высота левого поддерева для node больше правого более, чем на 1 (на 2)

            if (node.getLeftNode() != null && node.getLeftNode().getHeightDiff() < 0) {
                // двойной лево-правый поворот (LR-rotation)
                node.setLeftNode(leftRotate(node.getLeftNode()));
                node = rightRotate(node);
            } else {
                // правый поворот (R-rotation)
                node = rightRotate(node);
            }
        }
        return node;
    }

    private MapNode leftRotate(MapNode node) {
        MapNode right = node.getRightNode();
        node.setRightNode(right.getLeftNode());
        right.setLeftNode(node);
        node.recalcHeight();
        right.recalcHeight();
        return right;
    }

    private MapNode rightRotate(MapNode node) {
        MapNode left = node.getLeftNode();
        node.setLeftNode(left.getRightNode());
        left.setRightNode(node);
        node.recalcHeight();
        left.recalcHeight();
        return left;
    }
}
