package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Logic {

    //это для окнного интерфейса функция
    //входной список запишешь в табличку, из таблички вынешь в одномерный массив, и потом эту функцию вызовешь, тк с листом
    //удобнее работать :)
    public static ArrayList<Integer> arrayToList(int[] arr){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < arr.length; i++) list.add(arr[i]);
        return list;
    }

    //это функция с системным словарем, вам вроде надо ещё собственную реализацию словаря, поэтому нужна будет ещё одна
    //такая же абсолютно функция, только вместо new TreeMap<>() напишешь соломовский словарь
    public static ArrayList<Integer> withoutRepetition(ArrayList<Integer> list){
        ArrayList<Integer> result = new ArrayList<>();
        Map<Integer, Integer> map = new TreeMap<>();

        int i = 0;
        int count = 1; //счетчик
        while (list.size() != 0){
            for(int j = 1; j < list.size(); j++){
                if(list.get(i) == list.get(j)){
                    count++;
                    list.remove(j); //удаляем одинаковые элементы, уменьшая список, чтобы по сто раз по одним и тем же
                    //числам не проходить и не писать доп условие, есть ли в словаре этот ключ или нет)
                    j--; //см *
                }
            }
            map.put(list.get(i),count); //здесь ключ - число из списка, значение - количество повторений
            list.remove(i);
            count = 1;
        }
        for(Integer key : map.keySet()) result.add(key); //достаем массив ключей и записываем его в лист и вуаля -
        //список без повторений :)
        return result;
    }
}
/* при удалении элемента в списке индексы сразу пересчитываются, а именно: элементы 5 6 7 8 с индексами 0 1 2 3,
удалили 6: элементы 5 7 8 с индексами 0 1 2, то есть происходит пересчет, бегунок j=1, если его не откатить назад, то
при следующей итерации j будет равно 2, получается, мы пропустили 7, а при откате j становится равным 0 и при следующей
итерации j=1 вновь -> получаем нашу 7 (надеюсь, понятно объяснила :), но если что, то дэбаг в помощь)
*/