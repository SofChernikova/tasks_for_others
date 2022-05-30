 package ru.vsu.cs.course1.tree.bst.rb;

import java.awt.Color;
import ru.vsu.cs.course1.tree.BinaryTree;
import ru.vsu.cs.course1.tree.bst.BSTree;
import ru.vsu.cs.course1.tree.bst.BSTreeAlgorithms;

/**
 * Рализация красно-черного дерева:
 * https://cs.lmu.edu/~ray/notes/redblacktrees/
 * https://cs.lmu.edu/~ray/notes/binarysearchtrees/
 *
 * @param <T>
 */
public class RBTree<T extends Comparable<? super T>> implements BSTree<T> {

    static final boolean RED   = true;
    static final boolean BLACK = false;

    // не указываем модификаторы доступа, чтобы был доступ из того же пакета
    // (в частности из RBTreeMap)
    class RBTreeNode implements BinaryTree.TreeNode<T> {

        public T value;
        public boolean color;
        public RBTreeNode left;
        public RBTreeNode right;
        public RBTreeNode parent;

        public RBTreeNode(T value, boolean color, RBTreeNode left, RBTreeNode right, RBTreeNode parent) {
            this.value = value;
            this.color = color;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public RBTreeNode(T value, RBTreeNode parent) {
            this(value, BLACK, null, null, parent);
        }

        public RBTreeNode(T value) {
            this(value, null);
        }

        // Ниже идет реализация интерфейса BinaryTree.TreeNode<T>

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public BinaryTree.TreeNode<T> getLeft() {
            return left;
        }

        @Override
        public BinaryTree.TreeNode<T> getRight() {
            return right;
        }

        @Override
        public Color getColor() {
            return color == RED ? Color.RED : Color.BLACK;
        }
    }

    RBTreeNode root = null;
    private int size = 0;

    // Ниже идет реализация интерфейса BSTree<T> (@Override-методы)

    @Override
    public BinaryTree.TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T put(T value) {
        if (root == null) { //если дерева нема,то бах в корень
            setRoot(new RBTreeNode(value));
            size++;
            return null;
        }
        RBTreeNode node = root; //берем для начала в качестве узла корень
        while (true) {
            int cmp = value.compareTo(node.value); // сравниваем значение
            if (cmp == 0) {
                // в узле значение, равное value, перезаписывем и возвращаем старое
                T oldValue = node.value;
                node.value = value;
                return oldValue;
            } else if (cmp < 0) { //если наше значение меньше, идем налево
                if (node.left == null) { //если слева нема, то создаем красный узел и делаем корректировку
                    setLeft(node, new RBTreeNode(value));
                    size++;
                    correctAfterAdd(node.left);
                    return null;
                }
                node = node.left; //ну а если узел есть, то мы спускаемся на него  проверяем дальше
            } else {
                if (node.right == null) {
                    setRight(node, new RBTreeNode(value));
                    size++;
                    correctAfterAdd(node.right);
                    return null;
                }
                node = node.right;
            }
        }
    }

    @Override
    public T remove(T value) {
        RBTreeNode node = (RBTreeNode) getNode(value);
        if (node == null) {
            // в дереве нет такого значения
            return null;
        }
        T oldValue = node.value; //запомнили значение удаляемого узла
        if (node.left != null && node.right!= null) { //если есть оба потомка, то мы ищем минимальный элемент справа
            RBTreeNode nextValueNode = (RBTreeNode) BSTreeAlgorithms.getMinNode(node.right);
            //вместо удаляемого узла записываем минимальный справа
            node.value = nextValueNode.value;
            node = nextValueNode;
        }
        // здесь node имеет не более одного потомка
        RBTreeNode child = (node.left != null) ? node.left : node.right; //ищем, какой же потомок есть
        if (child != null) {
            if (node == root) { //если удаляемый элемент - корень, то мы его потомка делаем корнем и красим в черный
                setRoot(child);
                root.color = BLACK;
            } else if (node.parent.left == node) { //если наш удаляемый элемент - левый для своего родителя
                // child - левый потомок node
                setLeft(node.parent, child); //мы переключаем родителя на его ребёнка слева
            } else {
                // child - правый потомок node
                setRight(node.parent, child); //иначе переключаем на его ребенка справа
            }
            if (node.color == BLACK) {
                // если удалили черный узел, то нарушилось равновесие по черной высоте
                correctAfterRemove(child);
            }
        } else if (node == root) { //если нет потомков, просто удаляем узел
            root = null;
        } else {
            // если удалили черный узел, то нарушилось равновесие по черной высоте
            if (node.color == BLACK) {
                correctAfterRemove(node);
            }
            removeFromParent(node);
        }
        size--;
        return oldValue;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }


    /**
     * Классический алгоритм восстановления красно-черного дерева после добавления узла
     */
    private void correctAfterAdd(RBTreeNode node) {
        // шаг 1: Цвет узла - красный
        if (node != null) {
            node.color = RED;
        }

        // шаг 2: Корректировка двух подряд красных узлов (если имеет место быть)
        if (node != null && node != root && colorOf(node.parent) == RED) {

            // Step 2a (simplest): Recolor, and move up to see if more work
            // needed
            /* siblingOf-это дядя для нашего добавленного узла
            если дядя и батя красные - перекрашиваем в чёрный (первые 3 строчки),
            дедушку делаем красным и вызываем для дедушки ту же процедуру для него
            ЕСЛИ ДОБАВЛЯЕМ ЭЛЕМЕНТ СЛЕВА ОТ ПАПЫ, КОТОРЫЙ СЛЕВА ОТ ДЕДУШКИ, ПАПА И ДЯДЯ КРАСНЫЕ
             */
            if (isRed(siblingOf(parentOf(node)))) { //
                setColor(parentOf(node), BLACK);
                setColor(siblingOf(parentOf(node)), BLACK);
                setColor(grandparentOf(node), RED);
                correctAfterAdd(grandparentOf(node));
            }

            // Step 2b: Restructure for a parent who is the left child of the
            // grandparent. This will require a single right rotation if n is
            // also
            // a left child, or a left-right rotation otherwise.
            else if (parentOf(node) == leftOf(grandparentOf(node))) {
                if (node == rightOf(parentOf(node))) {
                    leftRotate(node = parentOf(node));
                }
                setColor(parentOf(node), BLACK);
                setColor(grandparentOf(node), RED);
                rightRotate(grandparentOf(node));
            }

            // Step 2c: Restructure for a parent who is the right child of the
            // grandparent. This will require a single left rotation if n is
            // also
            // a right child, or a right-left rotation otherwise.
            else if (parentOf(node) == rightOf(grandparentOf(node))) {
                if (node == leftOf(parentOf(node))) {
                    rightRotate(node = parentOf(node));
                }
                setColor(parentOf(node), BLACK);
                setColor(grandparentOf(node), RED);
                leftRotate(grandparentOf(node));
            }
        }

        // шаг 3: раскрасить корень дерева черным
        setColor(root, BLACK);
    }


    /**
     * Classic algorithm for fixing up a tree after removing a node; the
     * parameter to this method is the node that was pulled up to where the
     * removed node was.
     */
    private void correctAfterRemove(RBTreeNode node) {
        while (node != root && isBlack(node)) {
            if (node == leftOf(parentOf(node))) { //если узел наш слева от его родителя
                // Pulled up node is a left child
                RBTreeNode sibling = rightOf(parentOf(node)); //говорим, что дядя - справа
                if (isRed(sibling)) { //если дядя красный
                    setColor(sibling, BLACK); //меняем дядю на чёрный
                    setColor(parentOf(node), RED); //папу на красный
                    leftRotate(parentOf(node)); //левый поворот относительно папы
                    sibling = rightOf(parentOf(node)); //дядя теперь справа от папы (пап выше)
                }
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) { //если у дяди оба потомка чёрные
                    setColor(sibling, RED); //дядю делаем красным
                    node = parentOf(node);  //узел делаем папой ?????
                } else {
                    if (isBlack(rightOf(sibling))) { //если у дяди справа черный
                        setColor(leftOf(sibling), BLACK); //левому бахаем тоже чёрный
                        setColor(sibling, RED); //дядю делаем красным
                        rightRotate(sibling); //правый поворот относительно дяди
                        sibling = rightOf(parentOf(node)); //дядя теперь справа от папы нашего узла
                    } //это видимо если слева от дяди чёрный
                    setColor(sibling, colorOf(parentOf(node))); //дядю делаем такого же цвета, как батю
                    setColor(parentOf(node), BLACK); //перекрашиваем батю в чёрный
                    setColor(rightOf(sibling), BLACK); //спарва от дяди тоже черный
                    leftRotate(parentOf(node)); //левый поворот относительно бати
                    node = root; //узел теперь корень
                }
            } else {
                // pulled up node is a right child
                RBTreeNode sibling = leftOf(parentOf(node)); //дядя слева от папы
                if (isRed(sibling)) { //если дядя красный
                    setColor(sibling, BLACK); //меняем на чёрный
                    setColor(parentOf(node), RED); //батю на красный
                    rightRotate(parentOf(node)); //правый поворот для бати
                    sibling = leftOf(parentOf(node)); //дядя - левый потомок
                }
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) { //если дядя черный и
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (isBlack(leftOf(sibling))) {
                        setColor(rightOf(sibling), BLACK);
                        setColor(sibling, RED);
                        leftRotate(sibling);
                        sibling = leftOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(leftOf(sibling), BLACK);
                    rightRotate(parentOf(node));
                    node = root;
                }
            }
        }
        setColor(node, BLACK);
    }

    private void leftRotate(RBTreeNode node) {
        if (node.right == null) {
            return;
        }
        RBTreeNode right = node.right;
        setRight(node, right.left);
        if (node.parent == null) {
            setRoot(right);
        } else if (node.parent.left == node) {
            setLeft(node.parent, right);
        } else {
            setRight(node.parent, right);
        }
        setLeft(right, node);
    }

    private void rightRotate(RBTreeNode node) {
        if (node.left == null) {
            return;
        }
        RBTreeNode left = node.left;
        setLeft(node, left.right);
        if (node.parent == null) {
            setRoot(left);
        } else if (node.parent.left == node) {
            setLeft(node.parent, left);
        } else {
            setRight(node.parent, left);
        }
        setRight(left, node);
    }

    private void removeFromParent(RBTreeNode node) {
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = null;
            } else if (node.parent.right == node) {
                node.parent.right = null;
            }
            node.parent = null;
        }
    }


    // вспомогательные методы, сильно облегчающие жизнь
    // (в частности проверяют все на null)

    private boolean colorOf(RBTreeNode node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isRed(RBTreeNode node) {
        return colorOf(node) == RED;
    }

    private boolean isBlack(RBTreeNode node) {
        return colorOf(node) == BLACK;
    }

    private void setColor(RBTreeNode node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private void setLeft(RBTreeNode node, RBTreeNode left) {
        if (node != null) {
            node.left = left;
            if (left != null) {
                left.parent = node;
            }
        }
    }

    private void setRight(RBTreeNode node, RBTreeNode right) {
        if (node != null) {
            node.right = right;
            if (right != null) {
                right.parent = node;
            }
        }
    }

    private void setRoot(RBTreeNode node) {
        root = node;
        if (node != null) {
            node.parent = null;
        }
    }

    private RBTreeNode parentOf(RBTreeNode node) {
        return node == null ? null : node.parent;
    }

    private RBTreeNode grandparentOf(RBTreeNode node) {
        // return parentOf(parentOf(node));
        return (node == null || node.parent == null) ? null : node.parent.parent;
    }

    private RBTreeNode siblingOf(RBTreeNode node) {
        return (node == null || node.parent == null)
            ? null
            : (node == node.parent.left)
                ? node.parent.right : node.parent.left;
    }

    private RBTreeNode leftOf(RBTreeNode node) {
        return node == null ? null : node.left;
    }

    private RBTreeNode rightOf(RBTreeNode node) {
        return node == null ? null : node.right;
    }

    private int getHeight(RBTreeNode node) {
        return (node == null) ? -1 : Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    public int getHeight() {
        return getHeight(root);
    }
}
