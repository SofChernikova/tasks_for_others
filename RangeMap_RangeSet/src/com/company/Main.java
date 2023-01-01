package com.company;

import com.company.PartOfTree.MapEntry;
import com.company.PartOfTree.Range;

public class Main {

    public static void main(String[] args) {
        AVLTree map = new AVLTree();

        map.put(new MapEntry(new Range(30, 45), "BBB"));
        map.put(new MapEntry(new Range(18, 20), "ЗЗЗ"));
        map.put(new MapEntry(new Range(4, 5), "AAA"));
        map.put(new MapEntry(new Range(25, 28), "PPP"));
        map.put(new MapEntry(new Range(1, 3), "DDD"));
        map.put(new MapEntry(new Range(10, 12), "TTT"));
        map.put(new MapEntry(new Range(8, 9), "FFF"));
        map.put(new MapEntry(new Range(6, 7), "III"));

        TreeAlgorithms.printTree(map);
        map.remove(18);
        TreeAlgorithms.printTree(map);
        //System.out.println(map.getRoot().getMapEntry().getValue());
        //TreeAlgorithms.getMinNode(map.getRoot()).printNode();
        //TreeAlgorithms.SearchNode(map.getRoot(), 2);
        //TreeAlgorithms.SearchNode(map.getRoot(), 21);
        //TreeAlgorithms.IsSearch(map.getRoot(), 26);
    }
}
