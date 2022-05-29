package com.company;

import java.util.*;

public class StudentMarks implements Comparable<StudentMarks> {
    private Map<String, Integer> studMark;

    public StudentMarks() {
        this.studMark = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
    }

    public Map<String, Integer> add(String subjName, int mark) {
        studMark.put(subjName, mark);
        return studMark;
    }

    public Map<String, Integer> getStudMark() {
        return studMark;
    }

    @Override
    public int compareTo(StudentMarks o1) {
        if (this.studMark.entrySet().size() != o1.getStudMark().entrySet().size()) return -1;
// может быть ещё ситуация, когда предметов одинаковое количество, но они не все одинаковые, например:
// (алгебра, алхимия, матан) - (алгебра, дискретка, матан), но это опустим, пусть все предметы одинаковые
        else {
            Iterator<Map.Entry<String, Integer>> itr1 = this.studMark.entrySet().iterator();
            Iterator<Map.Entry<String, Integer>> itr2 = o1.studMark.entrySet().iterator();
            int count = 0;
            while (itr1.hasNext()) {
                Map.Entry<String, Integer> entry1 = itr1.next();
                Map.Entry<String, Integer> entry2 = itr2.next();
                if (entry1.getValue() == entry2.getValue()) {
                    count++;
                }
            }
            if(this.studMark.keySet().size() == count){
                return 0;
            }
            else return -1;
        }
    }
}
