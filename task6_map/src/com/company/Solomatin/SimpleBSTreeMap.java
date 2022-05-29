package com.company.Solomatin;

import com.company.Solomatin.interfaces.BSTree;
import com.company.Solomatin.interfaces.BSTreeMap;

public class SimpleBSTreeMap<K extends Comparable<K>, V> implements BSTreeMap<K, V> {

    private final BSTree<MapTreeEntry<K, V>> tree = new SimpleBSTree<>();

    @Override
    public BSTree<MapTreeEntry<K, V>> getTree() {
        return tree;
    }
}

