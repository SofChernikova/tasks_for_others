package com.company;

import com.company.Solomatin.SimpleBSTreeMap;
import com.company.StudentMarks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CreateMap {

    private String[][] readFromFile() throws IOException {
        Scanner scanner = null;
        String line = "";
        int index = 0;
        //int count = 1;
        int i = 0;

        BufferedReader reader = new BufferedReader(new FileReader("database.txt"));
        String[][] inputArr = new String[Integer.parseInt(reader.readLine())][3];

        while ((line = reader.readLine()) != null) {
            scanner = new Scanner(line);
            scanner.useDelimiter(" ");
            while (scanner.hasNext()) {
                String data = scanner.next();
                if (index == 0) {
                    inputArr[i][index] = data;
                    index++;
                    continue;
                }
                if (index == 1) {
                    inputArr[i][index] = data;
                    index++;
                    continue;
                }
                if (index == 2) {
                    inputArr[i][index] = data;
                    index++;
                    continue;
                }
            }
            i++;
            index = 0;
        }
        return inputArr;
    }

    public Map<String, StudentMarks> createMap() throws IOException {
        String[][] inputArr = readFromFile();
        Map<String, StudentMarks> dataBase = new TreeMap<>();
        boolean done = false;

        for (int j = 0; j < inputArr.length; j++) {
            if (dataBase.size() == 0) {
                StudentMarks mark = new StudentMarks();
                mark.add(inputArr[j][1], Integer.parseInt(inputArr[j][2]));
                dataBase.put(inputArr[j][0], mark);
            } else {
                Iterator<Map.Entry<String, StudentMarks>> itr = dataBase.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry<String, StudentMarks> entry = itr.next();
                    if (entry.getKey().equals(inputArr[j][0])) {
                        entry.getValue().add(inputArr[j][1], Integer.parseInt(inputArr[j][2]));
                        done = true;
                        break;
                    } else continue;
                }
                if (done) {
                    done = false;
                    continue;
                }
                else {
                    StudentMarks mark = new StudentMarks();
                    mark.add(inputArr[j][1], Integer.parseInt(inputArr[j][2]));
                    dataBase.put(inputArr[j][0], mark);
                    done = false;
                }
            }
        }
        return dataBase;
    }
    public Map<String, StudentMarks> createMyMap() throws IOException {
        String[][] inputArr = readFromFile();
        Map<String, StudentMarks> dataBase = new SimpleBSTreeMap<>();
        boolean done = false;

        for (int j = 0; j < inputArr.length; j++) {
            if (dataBase.size() == 0) {
                StudentMarks mark = new StudentMarks();
                mark.add(inputArr[j][1], Integer.parseInt(inputArr[j][2]));
                dataBase.put(inputArr[j][0], mark);
            } else {
                Iterator<Map.Entry<String, StudentMarks>> itr = dataBase.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry<String, StudentMarks> entry = itr.next();
                    if (entry.getKey().equals(inputArr[j][0])) {
                        entry.getValue().add(inputArr[j][1], Integer.parseInt(inputArr[j][2]));
                        done = true;
                    } else continue;
                }
                if (done) {
                    done = false;
                    continue;
                }
                else {
                    StudentMarks mark = new StudentMarks();
                    mark.add(inputArr[j][1], Integer.parseInt(inputArr[j][2]));
                    dataBase.put(inputArr[j][0], mark);
                    done = false;
                }
            }
        }
        return dataBase;
    }
}
