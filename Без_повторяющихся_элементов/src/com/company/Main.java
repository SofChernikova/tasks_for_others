package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(5);
        list.add(2);
        list.add(7);
        list.add(22);
        list.add(4);
        list.add(2);
        list.add(5);
        ArrayList<Integer> list1 = Logic.withoutRepetition(list);
        for(Integer i : list1){
            System.out.println(i);
        }
    }

}
