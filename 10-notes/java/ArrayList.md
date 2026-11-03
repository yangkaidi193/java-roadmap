---
type: note
topic: java
status: review        # 今天写完先标 review，周日自测过了再标 done
tags: [集合, arraylist]
created: 2026-11-02
source: JDK 21 源码 / 手写 MyArrayList 实践
---

# ArrayList 的核心机制

## 一句话结论

> ArrayList = Object[] 数组 + 1.5 倍扩容 + modCount 快速失败。查改 O(1)，中间增删 O(n)。

## 原理（自己画，不要贴别人的图）



- 存储：elementData 数组，默认容量 10，size 记录实际元素个数
  - 扩容时机：add 时 size == elementData.length 才扩容，不是"每加一个都检查一遍"之外的事
  - 扩容方式：newCapacity = old + old >> 1（1.5 倍），Arrays.copyOf 整块拷贝
  - fail-fast：modCount 每次结构性修改 +1，迭代器记录 expectedModCount，不一致就抛 ConcurrentModificationException
  - 删除：元素前移一位，最后位置 null（帮 GC 回收）

## 边界与坑

| 场景 | 会发生什么 | 怎么规避 |
|---|---|---|
| get(-1) | 我手写版会炸（没查负数），JDK 会抛 IndexOutOfBoundsException | 范围检查要查两端 |
| 迭代中 add | ConcurrentModificationException | 用迭代器的 remove，或收集后再改 |
| 1.5 倍扩容 | 比 2 倍省内存，但扩容更频繁 | 已知大小时用带容量的构造器 |
| 并发 add | 丢数据（不是线程安全的） | 用 CopyOnWriteArrayList 或加锁 |

## 我项目里在哪用到

> 健身预约系统的课程列表查询，MyBatis 返回的 List<Course> 默认就是 ArrayList。
> 场景特点：读多写少、大小可预估，正好是 ArrayList 的适用场景

## 追问 3 个（自己先答，再对照笔记）

1. 为什么扩容是 1.5 倍而不是 2 倍？（时间空间权衡，你答一遍）
2. fail-fast 能保证线程安全吗？（不能，只是快速报错机制——这个必考）
3. ArrayList 和 LinkedList 各自适合什么场景？

## 我原来以为 vs 其实

  - 原来以为：迭代器是独立的一个类。
    其实：内部类 Itr 持有外部类引用，直接读外部类的 elementData 和 modCount。
  - 原来以为：modCount 只是计数器。
    其实：它是 fail-fast 的哨兵，每次 next 都拿它比对。



## 三个月后自测

> 合上这份笔记，能不能手写出 add / grow / remove / 迭代器四个方法？能 → 标 done；不能 → 保持 review。
