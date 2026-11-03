public class MyLinkedList<E> {
    // 节点三件套：前驱、数据、后继（照抄）
    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;
        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> head;   // 头节点
    private Node<E> tail;   // 尾节点
    private int size;

    // 第1步：add(E e) 加到尾部（先做这个，最简单的）
    public boolean add(E e) {
        Node<E> newNode = new Node<>(tail, e, null);
        if (tail == null) {
            // TODO: 空链表 → head 和 tail 都指向新节点
            head = newNode;
            tail = newNode;
        } else {
            // TODO: tail.next 接新节点，然后 tail 后移
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }
    public int size() {
        return size;
    }


    // 第2步：node(index) 定位节点（给remove和get共用）
    private Node<E> node(int index) {
        // 优化：index < size/2 从 head 正着走，否则从 tail 倒着走
        // 先写正着走的版本跑通，优化之后再加
        if (index < size / 2) {
            Node<E> node = head;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        } else {
            Node<E> node = tail;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
            return node;
        }
    }

    // 第3步：get(index) = node(index).item（记得范围检查）
    public E get(int index) {
        // TODO: 范围检查
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // TODO: 返回 node(index).item
        return node(index).item;
    }

    // 第4步：remove(index)——今天的主菜
    public E remove(int index) {
        // 1. 范围检查（两端都查）
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        // 2. Node<E> n = node(index)
        Node<E> n = node(index);

        // 3. 按上面那张图重连，分三种情况：
        //    n.prev == null   → 删的是头：head = n.next; head.prev = null
        //    n.next == null   → 删的是尾：tail = n.prev; tail.next = null
        //    中间             → n.prev.next = n.next; n.next.prev = n.prev
        if (n.prev == null) {
            head = n.next;
            if (head == null) {
                tail = null;      // 删空了，tail也要清空
            } else {
                head.prev = null;
            }
        } else if (n.next == null) {
            tail = n.prev;
            tail.next = null;
        } else {
            n.prev.next = n.next;
            n.next.prev = n.prev;
        }

        // 4. size--; 返回 n.item
        size--;
        return n.item;
    }
    static void check(boolean cond, String name) {
        System.out.println((cond ? "PASS " : "FAIL ") + name);
    }

    public static void main(String[] args) {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.add(1); list.add(2); list.add(3);

        check(list.size() == 3, "size==3");
        check(list.get(1) == 2, "get(1)==2");

        int removed = list.remove(1);
        check(removed == 2 && list.get(0) == 1 && list.get(1) == 3, "删中间不断链");

        check(list.remove(0) == 1 && list.get(0) == 3, "删头部");

        check(list.remove(0) == 3 && list.size() == 0, "删到空");

        try {
            list.remove(0);
            check(false, "空链表remove抛异常");
        } catch (IndexOutOfBoundsException e) {
            check(true, "空链表remove抛异常");
        }

        // 这条专门抓你刚才那个bug
        list.add(1);
        check(list.remove(0) == 1, "删唯一元素不NPE");
        list.add(2);
        check(list.get(0) == 2 && list.size() == 1, "删空后再add");
    }
}