package ru.vsu.cs.course1.tree;

import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTreeAlgorithms {

    static int leftcounter = 0;
    static int allNodeCounter = 0;
    static int rootCounter = 0;



    public static int returnLeftcounter() {
        return leftcounter;
    }

    public static int newAllNodeCounter(){return allNodeCounter = 0; }
    public static int newRootCounter(){return rootCounter = 0;}

    public static int returnAllNodeCounter() { return allNodeCounter; }

    @FunctionalInterface
    public interface Visitor<T> {
        /**
         * "Посещение" значения
         *
         * @param value Значение, которое "посещаем"
         * @param level Уровень дерева/поддерева, на котором находится данное значение
         */
        void visit(T value, int level);
    }


    /**
     * Обход поддерева с вершиной в данном узле
     * "посетителем" в прямом/NLR порядке - рекурсивная реализация
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor  Посетитель
     *                 <p>
     *                 находимся на корне, посещаем -> можно пойти налево ? идем налево : идем направо,
     *                 посещаем -> можно пойти налево ? идем налево : идем направо, посещаем ......
     *                 так доходим до конца дерева, потом поднимаемся наверх до корня, его не посещаем и сворачиваем
     *                 направо, и опять можно пойти налево ? идем налево : идем направо, посещаем
     */
    public static <T> void preOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 3-мя параметрами)
        class Inner {
            void preOrderVisit(BinaryTree.TreeNode<T> node, Visitor<T> visitor, int level) {
                //   int first = (int)treeNode.getValue();
                if (node == null) {
                    return;
                }
                visitor.visit(node.getValue(), level);

               /* int temp = (int) node.getValue();
                if(temp < first){
                    leftcounter++;
                } else if(temp > first){
                    rightcounter++;
                } */
                preOrderVisit(node.getLeft(), visitor, level + 1);

                preOrderVisit(node.getRight(), visitor, level + 1);
            }
        }
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().preOrderVisit(treeNode, visitor, 0);
    }

    /**
     * Обход поддерева с вершиной в данном узле
     * в виде итератора в прямом/NLR порядке
     * (предполагается, что в процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> preOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            stack.push(treeNode); //создали стек, засунули туда корень

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return stack.size() > 0;
                } //пока в стеке есть что-то, вызывается next()

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop(); //выталкиваем последний элемент из стека и удаляем его


                    if (node.getRight() != null) {   //если есть справа элементы, кладем сначала правые
                        stack.push(node.getRight());
                    }
                    if (node.getLeft() != null) {  //затем кладём левые, тк прямой обход, и при таком подходе
                        stack.push(node.getLeft()); //как раз сначала будут выходить элементы слева, а потом уже справа
                    }
                    return node.getValue();
                }

            };
        };
    }

    public static class NodeCounts {
        public int leftNodes = 0, rightNodes = 0;
    };


    /**
     * Обход поддерева с вершиной в данном узле
     * "посетителем" в симметричном/поперечном/центрированном/LNR порядке - рекурсивная реализация
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     *   Посетитель
     *                 <p>
     *                 спускаемся максимально влево сначала (ну пока есть левые потомки), если больше левого потомка нет, посещаем узел и
     *                 поднимаемся на один выше, проверяем, есть ли справа элемент, если есть, идем туда и проверяем вновь левый элемент
     *                 (если есть идем туда и тд), если нет, то посещаем этот узел и поднимаемся вновь вверх, так доходим до корня
     *                 посещаем корень и идем вновь направо и вновь проверяем левый элемент и тд
     */
    public static <T> NodeCounts getLeftNodes(BinaryTree.TreeNode<T> treeNode) {
        final NodeCounts counts = new NodeCounts();
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 3-мя параметрами)
        class Inner {
            void inOrderVisit(BinaryTree.TreeNode<T> node,  int leftrotate, int rightrotate ) {
                // int first = (int)treeNode.getValue();

                if (node == null) {
                    return;
                }
                if(leftrotate > rightrotate)
                    counts.leftNodes++;
                else
                    counts.rightNodes++;
                inOrderVisit(node.getLeft(),  leftrotate+1,  rightrotate);
                inOrderVisit(node.getRight(),  leftrotate, rightrotate+1);
            }
        }
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().inOrderVisit(treeNode, 0,0);
        return counts;
    }

    public static <T> void inOrderLeftVisit(BinaryTree.TreeNode<T> treeNode, BinaryTree.TreeNode<T> root,  Visitor<T> visitor) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 3-мя параметрами)
        class Inner {
            void inOrderVisit(BinaryTree.TreeNode<T> node, BinaryTree.TreeNode<T> root, Visitor<T> visitor, int level) {
                // int first = (int)treeNode.getValue();
                if(node == root){
                    return;
                }
                if (node == null) {
                    return;
                }
                inOrderVisit(node.getLeft(),root,  visitor, level + 1);
                visitor.visit(node.getValue(), level);
                allNodeCounter++;
                inOrderVisit(node.getRight(), root,  visitor, level + 1);
            }
        }
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().inOrderVisit(treeNode, root, visitor, 0);
    }
    public static int countLeftNodes (BinaryTree.TreeNode<Integer> treeNode) {
        if(treeNode == null){ return 0;
        } else{
            countLeftNodes(treeNode.getLeft());
            leftcounter++;
        }
        return leftcounter;
    }
   /* public static int countRightNodes(BinaryTree.TreeNode<Integer> treeNode){
        if(treeNode == null){ return 0;
        } else{
            countRightNodes(treeNode.getRight());
            rightcounter++;
        }
        return rightcounter;
    } */

  /* public static <T> void inOrderVisitBeforeRoot(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 3-мя параметрами)
        class Inner {
            void inOrderVisit(BinaryTree.TreeNode<T> node, Visitor<T> visitor, int level) {
                    if (node == null) {
                        return;
                    }
               if (node == treeNode) rootCounter++;
                inOrderVisit(node.getLeft(), visitor, level + 1);
                visitor.visit(node.getValue(), level);
                leftcounter++;
                inOrderVisit(node.getRight(), visitor, level + 1);


            }
        }

        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().inOrderVisit(treeNode, visitor, 0);
    }*/

    /**
     * Обход поддерева с вершиной в данном узле в виде итератора в
     * симметричном/поперечном/центрированном/LNR порядке (предполагается, что в
     * процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> inOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            BinaryTree.TreeNode<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.getLeft(); //спускаемся как раз максимально влево
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop(); //достаем и удаляем самый левый
                    T result = node.getValue(); // запоминаем его значение
                    if (node.getRight() != null) { //если справа есть, идем направо
                        node = node.getRight();
                        while (node != null) {
                            stack.push(node); //вновь идем налево, пока есть слева узлы
                            node = node.getLeft();
                        }
                    }
                    return result;
                }
            };
        };
    }

    /**
     * Обход поддерева с вершиной в данном узле
     * "посетителем" в обратном/LRN порядке - рекурсивная реализация
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor  Посетитель
     */
    public static <T> void postOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 3-мя параметрами)
        class Inner {
            void postOrderVisit(BinaryTree.TreeNode<T> node, Visitor<T> visitor, int level) {
                if (node == null) {
                    return;
                }
                postOrderVisit(node.getLeft(), visitor, level + 1);
                postOrderVisit(node.getRight(), visitor, level + 1);
                visitor.visit(node.getValue(), level);
            }
        }
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().postOrderVisit(treeNode, visitor, 0);
    }

    /**
     * Обход поддерева с вершиной в данном узле в виде итератора в обратном/LRN порядке
     * (предполагается, что в процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> postOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            // Реализация TreeNode<T>, где left = right = null
            BinaryTree.TreeNode<T> emptyNode = () -> null;

            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            Stack<T> valuesStack = new Stack<>();
            stack.push(treeNode);

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return stack.size() > 0;
                }

                @Override
                public T next() {
                    for (BinaryTree.TreeNode<T> node = stack.pop(); node != emptyNode; node = stack.pop()) {
                        if (node.getRight() == null && node.getLeft() == null) {
                            return node.getValue();
                        }
                        valuesStack.push(node.getValue());
                        stack.push(emptyNode);
                        if (node.getRight() != null) {
                            stack.push(node.getRight());
                        }
                        if (node.getLeft() != null) {
                            stack.push(node.getLeft());
                        }
                    }
                    return valuesStack.pop();
                }
            };
        };
    }


    /**
     * Класс для хранения узла дерева вместе с его уровнем, нужен для методов
     * {@link #byLevelVisit(BinaryTree.TreeNode, Visitor)} и {@link #byLevelValues(BinaryTree.TreeNode)}
     *
     * @param <T>
     */
    private static class QueueItem<T> {
        public BinaryTree.TreeNode<T> node;
        public int level;

        public QueueItem(BinaryTree.TreeNode<T> node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    /**
     * Обход поддерева с вершиной в данном узле "посетителем" по уровням (обход в ширину)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor  Посетитель
     */
    public static <T> void byLevelVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        Queue<QueueItem<T>> queue = new LinkedList<>();
        queue.add(new QueueItem<>(treeNode, 0));
        while (!queue.isEmpty()) {
            QueueItem<T> item = queue.poll();
            if (item.node.getLeft() != null) {
                queue.add(new QueueItem<>(item.node.getLeft(), item.level + 1));
            }
            if (item.node.getRight() != null) {
                queue.add(new QueueItem<>(item.node.getRight(), item.level + 1));
            }
            visitor.visit(item.node.getValue(), item.level);
        }
    }

    /**
     * Обход поддерева с вершиной в данном узле в виде итератора по уровням (обход в ширину)
     * (предполагается, что в процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> byLevelValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Queue<QueueItem<T>> queue = new LinkedList<>();
            queue.add(new QueueItem<>(treeNode, 0));

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return queue.size() > 0;
                }

                @Override
                public T next() {
                    QueueItem<T> item = queue.poll();
                    if (item == null) {
                        // такого быть не должно, но на вский случай
                        return null;
                    }
                    if (item.node.getLeft() != null) {
                        queue.add(new QueueItem<>(item.node.getLeft(), item.level + 1));
                    }
                    if (item.node.getRight() != null) {
                        queue.add(new QueueItem<>(item.node.getRight(), item.level + 1));
                    }
                    return item.node.getValue();
                }
            };
        };
    }


    /**
     * Представление дерева в виде строки в скобочной нотации
     *
     * @param treeNode Узел поддерева, которое требуется представить в виже скобочной нотации
     * @return дерево в виде строки
     */
    public static <T> String toBracketStr(BinaryTree.TreeNode<T> treeNode) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 2-мя параметрами)
        class Inner {
            void printTo(BinaryTree.TreeNode<T> node, StringBuilder sb) {
                if (node == null) {
                    return;
                }
                sb.append(node.getValue());
                if (node.getLeft() != null || node.getRight() != null) {
                    sb.append(" (");
                    printTo(node.getLeft(), sb);
                    if (node.getRight() != null) {
                        sb.append(", ");
                        printTo(node.getRight(), sb);
                    }
                    sb.append(")");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().printTo(treeNode, sb);

        return sb.toString();
    }
}
