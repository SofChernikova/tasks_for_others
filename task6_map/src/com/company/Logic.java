package com.company;

import com.company.Solomatin.SimpleBSTreeMap;
import com.company.StudentMarks;

import java.util.*;

public class Logic {
    public Map<StudentMarks, List<String>> withTheSameMarks(Map<String, StudentMarks> map){
        //List<String> listName = new ArrayList<>();
        Map<StudentMarks, List<String>> result = new TreeMap<>();

        Iterator<Map.Entry<String, StudentMarks>> itr1 = map.entrySet().iterator();


        while (itr1.hasNext()) {
            Map.Entry<String, StudentMarks> entry1 = itr1.next();
            List<String> list = new ArrayList<>();
            list.add(entry1.getKey());
            Iterator<Map.Entry<String, StudentMarks>> itr2 = map.entrySet().iterator();
            while (itr2.hasNext()) {
                Map.Entry<String, StudentMarks> entry2 = itr2.next();
                if (entry1.getKey().equals(entry2.getKey())) continue;
                else {
                    if (entry1.getValue().compareTo(entry2.getValue()) == 0) {
                        list.add(entry2.getKey());
                    } else continue;
                }
            }
            if (list.size() > 1) {
                result.put(entry1.getValue(), list);
            }
        }
        return result;
    }

    public Map<StudentMarks, List<String>> withTheSameMarksMyMap(Map<String, StudentMarks> map){
        //List<String> listName = new ArrayList<>();
        Map<StudentMarks, List<String>> result = new TreeMap<>();

        Iterator<Map.Entry<String, StudentMarks>> itr1 = map.entrySet().iterator();


        while (itr1.hasNext()) {
            Map.Entry<String, StudentMarks> entry1 = itr1.next();
            List<String> list = new ArrayList<>();
            list.add(entry1.getKey());
            Iterator<Map.Entry<String, StudentMarks>> itr2 = map.entrySet().iterator();
            while (itr2.hasNext()) {
                Map.Entry<String, StudentMarks> entry2 = itr2.next();
                if (entry1.getKey().equals(entry2.getKey())) continue;
                else {
                    if (entry1.getValue().compareTo(entry2.getValue()) == 0) {
                        list.add(entry2.getKey());
                    } else continue;
                }
            }
            if (list.size() > 1) {
                result.put(entry1.getValue(), list);
            }
        }
        return result;
    }

    public void print(Map<StudentMarks, List<String>> map){
       int count = 0;
        for (StudentMarks marks : map.keySet()) {
            Map<String, Integer> studMark = marks.getStudMark();
            count = studMark.size();
            for (String s : studMark.keySet()) {
                if (count > 1){
                    System.out.print(s + ", " + "'" + studMark.get(s) + "'" + "; ");
                    count --;
                }
                else System.out.print(s + ", " + "'" + studMark.get(s) + "'" + " -> ");
            }
            System.out.println(map.get(marks));
        }
    }
}
