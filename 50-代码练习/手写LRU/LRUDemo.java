import java.util.LinkedHashMap;
import java.util.Map;

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);   // 第3个参数true = 按访问顺序排链表
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;       // 超过容量，LinkedHashMap自动删掉链表头
    }
}

public class LRUDemo {
    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        System.out.println(cache);          // {1=A, 2=B, 3=C}

        cache.get(1);                       // 访问1 → 1被移到尾部，变成"最新"
        System.out.println(cache);          // {2=B, 3=C, 1=A}

        cache.put(4, "D");                  // 超容量 → 自动淘汰链表头（最久没用的2）
        System.out.println(cache);          // {3=C, 1=A, 4=D}

        System.out.println(cache.containsKey(2));   // false ← 验收关键断言
        System.out.println(cache.containsKey(1));   // true  ← 1因为刚访问过，活下来了
    }
}