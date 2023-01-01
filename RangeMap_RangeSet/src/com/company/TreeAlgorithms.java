package com.company;

import com.company.PartOfTree.MapNode;

import java.util.Stack;

public class TreeAlgorithms {

    /*public static MapNode getNode(MapNode node, int k) {
        if (node == null) {
            return null;
        }
        int cmp = node.getMapEntry().compareTo(entry);
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return getNode(node.getLeftNode(), entry);
        } else {
            return getNode(node.getRightNode(), entry);
        }
    }*/
    public static MapNode getNode(MapNode node, int value){
        if (node == null){
            return null;
        }
        if (value >= node.getMapEntry().getKey().getLeft() && value <= node.getMapEntry().getKey().getRight()){
            return node;
        }
        else if (value < node.getMapEntry().getKey().getLeft()){
            return getNode(node.getLeftNode(), value);
        }else{
            return getNode(node.getRightNode(), value);
        }
    }

    public static void searchNode(MapNode root, int x) { //Вывод искомого нода
        if (root == null) {
            System.out.println("Дерево пустое");
        }
        MapNode node = root;
        boolean isSearch = false;
        while (isSearch == false && node != null) {
            if (x >= node.getMapEntry().getKey().getLeft() && x <= node.getMapEntry().getKey().getRight()) {
                //node.printRange();
                node.printNode();
                isSearch = true;
            } else if (x <= node.getMapEntry().getKey().getLeft()) {
                node = node.getLeftNode();
            } else {
                node = node.getRightNode();
            }
        }
        if (isSearch == false){
            System.out.println("Такого элемента нет");
        }
    }

    public static void IsSearch(MapNode root, int x){ //Проверка, существует ли нод
        if (root == null) {
            System.out.println("Дерево пустое");
        }
        MapNode node = root;
        boolean isSearch = false;
        while (isSearch == false && node != null) {
            if (x >= node.getMapEntry().getKey().getLeft() && x <= node.getMapEntry().getKey().getRight()) {
                //node.printRange();
                System.out.println("True");
                isSearch = true;
            } else if (x <= node.getMapEntry().getKey().getLeft()) {
                node = node.getLeftNode();
            } else{
                node = node.getRightNode();
            }
        }
        if (isSearch == false) {
            System.out.println("False");
        }
    }

    public static MapNode getMinNode(MapNode node) {
        return (node == null || node.getLeftNode() == null) ? node : getMinNode(node.getLeftNode());
    }

    public static void printTree(AVLTree map){
        Stack globalStack = new Stack();
        MapNode root = map.getRoot();
        globalStack.push(root);
        int gaps = 32;
        boolean isRowEmpty = false;
        String separator = "---------------------------------";
        System.out.println(separator);
        while (isRowEmpty == false){
            Stack localStack = new Stack();
            isRowEmpty = true;
            for (int j = 0; j<gaps; j++)
                System.out.print(' ');
            while (globalStack.isEmpty()==false){
                MapNode temp = (MapNode) globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.getMapEntry().getKey().getLeft() + "..." + temp.getMapEntry().getKey().getRight());
                    //temp.printRange();
                    localStack.push(temp.getLeftNode());
                    localStack.push(temp.getRightNode());
                    if (temp.getLeftNode() != null || temp.getRightNode() != null) {
                        isRowEmpty = false;
                    }
                }
                else {
                    System.out.print("__");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j< gaps*2-2; j++) {
                    System.out.print(' ');
                }
            }
            System.out.println();
            gaps /= 2;
            while (localStack.isEmpty() == false){
                globalStack.push(localStack.pop());
            }
        }
        System.out.println(separator);
    }
}
