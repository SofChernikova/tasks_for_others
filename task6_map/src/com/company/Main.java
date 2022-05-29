package com.company;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        CreateMap createMap = new CreateMap();
        Map<String, StudentMarks> systemDataBase = createMap.createMap();
        Logic logic = new Logic();
        Map<StudentMarks, List<String>> systemResult = logic.withTheSameMarks(systemDataBase);
        logic.print(systemResult);
        System.out.println("---------------------------------------------------------------------------");

        Map<String, StudentMarks> dataBase = createMap.createMyMap();
        Map<StudentMarks, List<String>> result = logic.withTheSameMarksMyMap(dataBase);
        logic.print(result);

    }
}
