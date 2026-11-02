import java.util.Arrays;
import java.util.Iterator;
import java.util.ConcurrentModificationException;

public class MyArrayList<E> implements Iterable<E> {
    private Object[] elementData;  // 存数据的数组
    private int size;              // 当前元素个数
    private int modCount;          // 修改次数，fail-fast的哨兵

    // 第1步：构造方法，new一个长度10的数组（先不写带参构造）
    public MyArrayList() {
        elementData = new Object[10];
    }

    // 第2步：add(E e)：先判断 size == elementData.length 就调grow()，再放元素，size+1
    //        每步跑一次：System.out.println测试加3个元素
    public boolean add(E e) {
        if (size == elementData.length) {
            grow();
        }
        elementData[size++] = e;
        modCount++;
        return true;
    }


    // 第3步：grow()：新数组长度 = 旧的 + 旧的的一半（>> 1），System.arraycopy拷贝
    //        测试：循环塞15个元素，看会不会炸
    private void grow() {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    // 第4步：get(index)：下标 >= size 抛 IndexOutOfBoundsException
    public E get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elementData[index];
    }
    //        第5步：remove(index)：范围检查、后面元素往前移一位、最后一位置null、size-1
    public E remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = (E) elementData[index];
        for (int i = index; i < size - 1; i++) {
            elementData[i] = elementData[i + 1];
        }
        elementData[size - 1] = null;
        size--;
        modCount++;
        return oldValue;
    }

    // 第6步：size()返回size
    public int size() {
        return size;
    }

    // 第7步：迭代器：内部类 Itr 实现 Iterator
    private class Itr implements Iterator<E> {
        //        hasNext()看cursor是否走到size；next()取元素、cursor前进；
        int cursor ;
        int lastRet = -1;
        int expectedModCount = modCount;
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            checkForComodification();
            return (E) elementData[cursor++];
        }
        //        每次操作先检查 modCount 和期望值是否一致，不一致抛 ConcurrentModificationException
        final void checkForComodification() {
            if (modCount != expectedModCount) // 👈 比较！
                throw new ConcurrentModificationException(); // 👈 抛出异常！
        }
    }
    public Iterator<E> iterator() {
        return new Itr();
    }

    public static void main(String[] args) {
        MyArrayList<Integer> list = new MyArrayList<>();

        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println("size = " + list.size());   // 3
        System.out.println("get(0) = " + list.get(0)); // 1
        // 1. 加3个元素后 size==3
        check(list.size() == 3, "size==3");

        // 2. get取值正确
        check(list.get(0) == 1, "get(0)==1");

        // 3. 循环塞15个触发扩容，扩容后数据完好
        for (int i = 4; i <= 15; i++) list.add(i);
        check(list.size() == 15, "扩容后size==15");
        check(list.get(14) == 15, "扩容后get(14)==15");

        // 4. 越界抛异常（用try-catch验证，catch到就是PASS）
        try {
            list.get(15);
            check(false, "越界get抛异常");
        } catch (IndexOutOfBoundsException e) {
            check(true, "越界get抛异常");
        }

        // 5. remove返回被删元素、size减一、后面元素前移
        int removed = list.remove(0);
        check(removed == 1 && list.size() == 14 && list.get(0) == 2, "remove前移");

        // 6. for-each能遍历（这一步验证你的iterator()方法）
        int sum = 0;
        for (int v : list) sum += v;
        check(sum == 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12 + 13 + 14 + 15, "for-each遍历");

        // 7. 迭代中修改抛 ConcurrentModificationException
        try {
            for (int v : list) list.add(99);
            check(false, "fail-fast抛异常");
        } catch (ConcurrentModificationException e) {
            check(true, "fail-fast抛异常");
        }
    }
    static void check(boolean cond, String name) {
        System.out.println((cond ? "PASS " : "FAIL ") + name);
    }



}