package ru.vsu.cs.course1.tree.bst.avl;

import ru.vsu.cs.course1.tree.BinaryTree;
import ru.vsu.cs.course1.tree.bst.BSTree;
import ru.vsu.cs.course1.tree.bst.BSTreeAlgorithms;

/**
 * Рализация AVL-дерева:
 * http://qaru.site/questions/1973343/avl-tree-rotation-in-java
 *
 * @param <T>
 */
public class AVLTree<T extends Comparable<? super T>> implements BSTree<T> {

    // не указываем модификаторы доступа, чтобы был доступ из того же пакета
    // (в частности из AVLTreeNode и AVLTreePainter)
    class AVLTreeNode implements BinaryTree.TreeNode<T> {

        public T value;
        public AVLTreeNode left;
        public AVLTreeNode right;
        public int height;

        public AVLTreeNode(T value, AVLTreeNode left, AVLTreeNode right, int height) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = height;
        }

        public AVLTreeNode(T value) {
            this(value, null, null, 0);
        }

        public void recalcHeight() {
            height = Math.max((left == null ? -1 : left.height), (right == null ? -1 : right.height)) + 1;
        }

        public int getHeightDiff() {
            return (left == null ? -1 : left.height) - (right == null ? -1 : right.height);
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
    }

    AVLTreeNode root = null;
    private int size = 0;

    /**
     * Рекурсивное добавление значения в поддерево node
     *
     * @param node Узел, в который (в него или его поддеревья) добавляем
     * значение value
     * @param value Добавляемое значение
     * @return Узел, который должен оказаться на месте node
     */
    private AVLTreeNode put(AVLTreeNode node, T value) {
        /*простой случай, когда у нас после узла ничего нет, мы тогда создаем новый узел со значением, которое
        мы хотели бы вставить
         */
        if (node == null) {
            size++;
            return new AVLTreeNode(value);
        }
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            // в узле значение, равное value, просто перезаписываем
            node.value = value;
        } else {
            if (cmp < 0) {
                node.left = put(node.left, value);
                /*присваиваем значение, которое вернёт put при вставке элемента в левое поддерево
                если ничего не поменятеся, то узел вернётся тот же самый, а если произойдет балансировка,
                то сюда придет узел, который вернёт балансировка (корень этого поддерева, которое раньше было node.left)*/
            } else {
                node.right = put(node.right, value);
            }
            node.recalcHeight(); //пересчёт высот
            // балансировка, вызывается всегда,но выполняется только если есть дисбаланс
            node = balance(node);
        }
        return node; //возвращаем то, что вернёт balance
    }

    private AVLTreeNode remove(AVLTreeNode node, T value)
    {
        if (node == null) {
            return null;
        }
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            // в узле значение, равное value
            if (node.left != null && node.right != null) {
                node.value = BSTreeAlgorithms.getMinNode(node.right).getValue();
                node.right = remove(node.right, node.value);
            } else {
                node = (node.left != null) ? node.left : node.right;
                size--;
            }
        } else if (cmp < 0)
            node.left = remove(node.left, value);
        else {
            node.right = remove(node.right, value);
        }
        return balance(node);
    }

    private AVLTreeNode balance(AVLTreeNode node) {
        if (node == null) {
            return null;
        }
        if (node.getHeightDiff() < -1) {
            // высота правого поддерева для node больше левого более, чем на 1 (на 2)

            if (node.right != null && node.right.getHeightDiff() > 0) {
                // двойной право-левый поворот (RL-rotation)
                node.right = rightRotate(node.right);
                node = leftRotate(node);
            } else {
                // левый поворот (L-rotation)
                node = leftRotate(node);
            }
        } else if (node.getHeightDiff() > 1) {
            // высота левого поддерева для node больше правого более, чем на 1 (на 2)

            if (node.left != null && node.left.getHeightDiff() < 0) {
                // двойной лево-правый поворот (LR-rotation)
                node.left = leftRotate(node.left);
                node = rightRotate(node);
            } else {
                // правый поворот (R-rotation)
                node = rightRotate(node);
            }
        }
        return node;
    }

    private AVLTreeNode leftRotate(AVLTreeNode node) {
        AVLTreeNode right = node.right; //взяли правого потомка узла, для которого нужен поворот
        node.right = right.left; //вместо правого потомка узла, для которого нужен поворот, записали левого потомка right
        right.left = node; /*а вместо левого потомка right написали узел, для которого был нужен поворот (он уже будет
         иметь в качетсве правого потомка левого потомка right )*/
        node.recalcHeight();
        right.recalcHeight();
        return right;
    }

    private AVLTreeNode rightRotate(AVLTreeNode node) {
        AVLTreeNode left = node.left; //взяли левого потомка узла, для которого нужен поворот
        node.left = left.right; //вместо левого потомка узла, для которого нужен поворот, записали правого потомка left
        left.right = node; /*а вместо правого потомка left написали узел, для которого был нужен поворот (он уже будет
         иметь в качетсве левого потомка правого потомка left )*/
        node.recalcHeight(); //пересчитали высоту
        left.recalcHeight();
        return left;
    }

    // Ниже идет реализация интерфейса BSTree<T> (@Override-методы)

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T put(T value) {
        // крайне неэффективная реализация, т.к. поиск по дереву иде 2 раза (надо переписать)
        T oldValue = this.get(value);
        this.root = put(root, value);
        return oldValue;
    }

    @Override
    public T remove(T value) {
        // крайне неэффективная реализация, т.к. поиск по дереву иде 2 раза (надо переписать)
        T oldValue = this.get(value);
        this.root = remove(root, value);
        return oldValue;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }
}
