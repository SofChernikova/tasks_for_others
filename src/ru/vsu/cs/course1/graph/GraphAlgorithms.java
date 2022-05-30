package ru.vsu.cs.course1.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

public class GraphAlgorithms {

    /**
     * Поиск в глубину, реализованный рекурсивно
     * (начальная вершина также включена)
     *
     * @param graph   граф
     * @param from    Вершина, с которой начинается поиск
     * @param visitor Посетитель
     */
    public static class visitVertices {
        public int visitVertices = 0;
    }

    public static int[] dfsRecursion(Graph graph, int from, Consumer<Integer> visitor) {
        boolean[] visited = new boolean[graph.vertexCount()]; //создаём массив, отображающий - посетили или нет вершины
        int[] vertices = new int[graph.vertexCount()];
        int[] unreachable = new int[graph.vertexCount()];
        for(int i = 0; i<vertices.length; i++){
            vertices[i] = -1;
        }
       // final visitVertices count = new visitVertices();
        class Inner {
            void visit(Integer curr) {
                visitor.accept(curr); //говорим, что посетили текущую
                visited[curr] = true; //записываем это в наш массива
                for (Integer v : graph.adjacencies(curr)) { //цикл по смежным вершинам
                    if (!visited[v]) { //если в массиве не написано, что посетили, посещаем
                        vertices[v] = curr;
                        visit(v);
                        //count.visitVertices++;
                    }
                }
            }
        }
        new Inner().visit(from); //создаем новый объект класса, начиная с первого элемента (в нашем случае9)
       // return count;
        for(int i = from; i < vertices.length; i++){
            if(vertices[i] == -1){
                unreachable[i] = vertices[i];
            } else continue;
        }
        return unreachable;
    }

    /**
     * Поиск в глубину, реализованный с помощью стека
     * (не совсем "правильный"/классический, т.к. "в глубину" реализуется только "план" обхода, а не сам обход)
     *
     * @param graph   граф
     * @param from    Вершина, с которой начинается поиск
     * @param visitor Посетитель
     */
    public static void dfs(Graph graph, int from, Consumer<Integer> visitor) {
        boolean[] visited = new boolean[graph.vertexCount()]; //создаём массив, отображающий - посетили или нет вершины
        Stack<Integer> stack = new Stack<Integer>(); //стэк
        stack.push(from); //кладём первый элемент
        visited[from] = true; //говорим, что посетили его
        while (!stack.empty()) { //пока есть в стэке элементы
            Integer curr = stack.pop(); // достаем последний положенный
            visitor.accept(curr); // посетили текущий элемент
            for (Integer v : graph.adjacencies(curr)) { //цикл по смежным вершинам с текущей
                if (!visited[v]) { //если мы ещё эти вершины не посещали
                    stack.push(v); //кладем их в стэк
                    visited[v] = true; //говорим, что она теперь посещённая
                }
            }
        }
    }

    /**
     * Поиск в ширину, реализованный с помощью очереди
     * (начальная вершина также включена)
     *
     * @param graph   граф
     * @param from    Вершина, с которой начинается поиск
     * @param visitor Посетитель
     */
    public static void bfs(Graph graph, int from, Consumer<Integer> visitor) {
        boolean[] visited = new boolean[graph.vertexCount()];
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(from);
        visited[from] = true;
        while (queue.size() > 0) {
            Integer curr = queue.remove();
            visitor.accept(curr);
            for (Integer v : graph.adjacencies(curr)) {
                if (!visited[v]) {
                    queue.add(v);
                    visited[v] = true;
                }
            }
        }
    }

    /**
     * Поиск в глубину в виде итератора
     * (начальная вершина также включена)
     *
     * @param graph граф
     * @param from  Вершина, с которой начинается поиск
     * @return Итератор
     */
    public static Iterable<Integer> dfs(Graph graph, int from) {
        return new Iterable<Integer>() {
            private Stack<Integer> stack = null;
            private boolean[] visited = null;

            @Override
            public Iterator<Integer> iterator() {
                stack = new Stack<>();
                stack.push(from);
                visited = new boolean[graph.vertexCount()];
                visited[from] = true;

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return !stack.isEmpty();
                    }

                    @Override
                    public Integer next() {
                        Integer result = stack.pop();
                        for (Integer adj : graph.adjacencies(result)) {
                            if (!visited[adj]) {
                                visited[adj] = true;
                                stack.add(adj);
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }

    /**
     * Поиск в ширину в виде итератора
     * (начальная вершина также включена)
     *
     * @param from Вершина, с которой начинается поиск
     * @return Итератор
     */
    public static Iterable<Integer> bfs(Graph graph, int from) {
        return new Iterable<Integer>() {
            private Queue<Integer> queue = null;
            private boolean[] visited = null;

            @Override
            public Iterator<Integer> iterator() {
                queue = new LinkedList<>();
                queue.add(from);
                visited = new boolean[graph.vertexCount()];
                visited[from] = true;

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return !queue.isEmpty();
                    }

                    @Override
                    public Integer next() {
                        Integer result = queue.remove();
                        for (Integer adj : graph.adjacencies(result)) {
                            if (!visited[adj]) {
                                visited[adj] = true;
                                queue.add(adj);
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }
}
