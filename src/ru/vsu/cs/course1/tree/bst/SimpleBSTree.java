package ru.vsu.cs.course1.tree.bst;

import java.util.function.Function;
import ru.vsu.cs.course1.tree.SimpleBinaryTree;
import ru.vsu.cs.course1.tree.BinaryTree;

/**
 * Класс, реализующий простое (наивное) дерево поиска
 * @param <T>
 */
public class SimpleBSTree<T extends Comparable<? super T>> extends SimpleBinaryTree<T> implements BSTree<T> {

    private static class CheckBSTResult<T> {
        public boolean result; //является ли то дерево, для которого мы вызвали проверку, BST
        public int size;
        public T min; //минимум в этом дереве
        public T max; //максимум в этом дереве
        static int level;


        public CheckBSTResult(boolean result, int size, T min, T max) {
            this.result = result;
            this.size = size;
            this.min = min;
            this.max = max;
           // this.level = level;
        }
    }


    int size = 0;

    public SimpleBSTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        super(fromStrFunc, toStrFunc);
    }

    public SimpleBSTree(Function<String, T> fromStrFunc) {
        super(fromStrFunc);
    }

    public SimpleBSTree() {
        super();
    }

    private static <T extends Comparable<? super T>> CheckBSTResult<T> isBSTInner(BinaryTree.TreeNode<T> node) {
        if (node == null) {
            return null;
        }
        /*анализ происходит рекурсивно по уровням поддеревьев
        для левого поддерева каждого узла нам надо найти максимум, для правого - минимум
        если для каждого узла максимум слева меньше и минимум справа больше, то дерево - BST */
        CheckBSTResult<T> leftResult = isBSTInner(node.getLeft());
        CheckBSTResult<T> rightResult = isBSTInner(node.getRight());
        CheckBSTResult<T> result = new CheckBSTResult<>(true, 1, node.getValue(), node.getValue());
        if (leftResult != null) {
            result.result &= leftResult.result; /*присваем true/false в зависимости от того, является ли
            поддерево данного узла BST (тип допустим левое поддерево (считаем от корня) в каких-то ветках не
             является деревом поиска => всё дерево - не BST)*/
            result.result &= leftResult.max.compareTo(node.getValue()) < 0; //проверка: максимум меньше самого узла
            result.size += leftResult.size;
            result.min = leftResult.min;
        }
        if (rightResult != null) {
            result.result &= rightResult.result;
            result.size += rightResult.size;
            result.result &= rightResult.min.compareTo(node.getValue()) > 0;
            result.max = rightResult.max;
        }
        /*так мы проверяем левое и правое поддеревья, пока не дойдем до корня. Когда дойдем, нам слева вернётся
        результат (бинарное поиска поддерево или нет), максимальное значение, которое там было найдено
        справа вернётся то же самое, только минимальное значение, потом мы сравниваем эти два значения с корнем
        если всё ок - дерево BST*/
        return result;
    }

    /**
     * Проверка, является ли поддерево деревом поиска
     * просто чтобы скрыть сложность для пользователя
     * @param <T>
     * @param node Поддерево
     * @return treu/false
     */
    public static <T extends Comparable<? super T>> boolean isBST(BinaryTree.TreeNode<T> node) {
        return node == null ? true : isBSTInner(node).result;
    }

    /**
     * Загрузка дерева из скобочного представления
     * @param bracketStr
     * @throws Exception Если переаддное скобочное представление не является деревом поиска
     */
    @Override
    public void fromBracketNotation(String bracketStr) throws Exception {
        SimpleBinaryTree tempTree = new SimpleBinaryTree(this.fromStrFunc);
        tempTree.fromBracketNotation(bracketStr);
        CheckBSTResult<T> tempTreeResult = isBSTInner(tempTree.getRoot());
        if (!tempTreeResult.result) {
            throw new Exception("Заданное дерево не является деревом поиска!");
        }
        super.fromBracketNotation(bracketStr);
        this.size = tempTreeResult.size;
    }

    /**
     * Рекурсивное добавление значения в поддерево node
     *
     * @param node Узел, в который (в него или его поддеревья) добавляем
     * значение value
     * @param value Добавляемое значение
     * @return Старое значение, равное value, если есть
     */
    private T put(SimpleTreeNode node, T value) {
        int cmp = value.compareTo(node.value);
        /*мы перезаписываем значение здесь из-за того, что например дерево из словаря использует два поля, напрмер,
         ключ-строку и значение-инт, сравнение по ключу вернет 0, сравнение по значению вернет 1 или -1 (названия
         одинаковые, начинки разные) -> поэтому лучше перезаписать (return oldValue по той же причине)*/
        if (cmp == 0) {
            // в узле значение, равное value
            T oldValue = node.value;
            node.value = value;
            return oldValue;
        } else {
            //наше значение меньше этого узла? идем налево : идем направо
            if (cmp < 0) {
                if (node.left == null) {
                    node.left = new SimpleTreeNode(value); //-> создаем узел, если там ничего нет
                    size++; //увеличиваем размер нашего дерева
                    return null; //в качестве старого значения возвращаем null, ведь ничего не было
                } else {
                    return put(node.left, value);
                    /*если слева не null, то мы рекурсивно вызываем ещё раз функцию
                    для левого узла*/
                }
            } else {
                if (node.right == null) {
                    node.right = new SimpleTreeNode(value);
                    size++;
                    return null;
                } else {
                    return put(node.right, value);
                }
            }
        }
    }

    /**
     * Рекурсивное удаления значения из поддерева node
     *
     * @param node
     * @param nodeParent Родитель узла
     * @param value
     * @return Старое значение, равное value, если есть
     */
    private T remove(SimpleTreeNode node, SimpleTreeNode nodeParent, T value )
    {
        if (node == null) { //узел не нашли, выходим, ничего не удаляем
            return null;
        }
       int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            // в узле значение, равное value
            T oldValue = node.value;
            if (node.left != null && node.right != null) {
                /*
                написала для минимального элемента справа, но можно ещё искать макисмальный элемент
                слева, его ставить вместо удаляемого, а вместо максимального поставить то, на что
                он ссылается */

                // если у node есть и левое и правое поддерево
                SimpleTreeNode minParent = getMinNodeParent(node.right); //ищем батьку минимального справа
                if (minParent == null) {
                    node.value = node.right.value;
                    node.right = node.right.right;
                } else {
                    /* если мы нашли такое значение, то мы нашему узлу, который удаляем, присваиваем значение
                    минимального справа (не батькино значение, а его сына, сын будет на батьку указывать, получается)
                     */
                    node.value = minParent.left.value;
                    /* если у сына есть ещё дети, то мы берем в качестве минимального элемента (вместо сына)
                    правого потомка этого узла (отдаем внуков дедушке, так сказать, пока батя там правит наверху)
                     */
                    minParent.left = minParent.left.right;

                }
            } else {
                /* если есть только одно поддерево, то мы вместо удаляемого элемента целиком подвешиваем это поддерево
                тип child - поддерево, которое у нас есть
                если мы удаляем корневой элемент, то у нас первый элемент из имеющегося поддерва становится корнем
                если есть какие-то родители (правое или левое у удаляемого элемента) то мы просто ветку их переключаем
                на существующее поддерево
                 */
                SimpleTreeNode child = (node.left != null) ? node.left : node.right;
                if (nodeParent == null) {
                    // возможно, если только node == root
                    root = child;
                } else if (nodeParent.left == node) {
                    nodeParent.left = child;
                } else {
                    nodeParent.right = child;
                }
            }
            size--;
            return oldValue;
        } else if (cmp < 0) //если занчение меньше, чем узел, идем влево
            return remove(node.left, node, value);
        else {
            return remove(node.right, node, value); //иначе идем вправо
        }
    }

    /**
     * Поиск родителя минимально TreeNode в поддереве node
     * это тип нашли минимальный и отдали нам элемент на один выше - его батьку
     * @param node Поддерево в котором надо искать родителя минимального элемент
     * @return Узел, содержащий минимальный элемент
     */
    private SimpleTreeNode getMinNodeParent(SimpleTreeNode node) {
        if (node == null) {
            return null;
        }
        SimpleTreeNode parent = null;
        for (; node.left != null; node = node.left) {
            parent = node;
        }
        return parent;
    }

    // Реализация BSTree<T>

    @Override
    public T put(T value) {
        if (root == null) {
            root = new SimpleTreeNode(value);
            size++;
            return null;
        }
        return put(root, value);
    }

    @Override
    public T remove(T value) {
        return remove(root, null, value);
    }

    @Override
    public int size() {
        return size;
    }
}
