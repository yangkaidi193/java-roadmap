---
type: note
topic: java
status: review
tags: [集合, linkedlist]
created: 2026-11-04
source: JDK 21 源码 / 手写 MyLinkedList 实践
---

# LinkedList 的核心机制

## 一句话结论

> LinkedList = 双向链表 + 头尾指针。头尾增删 O(1)，"中间增删"整体还是 O(n)——因为要先遍历定位。

## 原理

- Node 三件套：prev / item / next，节点之间靠指针相连，没有数组
- add：新节点挂在 tail 后面，tail 后移；空链表时 head、tail 都指向它
- node(index)：JDK 优化是 index < size/2 从 head 正着走，否则从 tail 倒着走
- remove：两行核心——n.prev.next = n.next; n.next.prev = n.prev，让左右邻居互相认识；删头/删尾/删唯一要特殊处理

## 边界与坑

| 场景 | 会发生什么 | 怎么规避 |
|---|---|---|
| 删除唯一元素 | head = null 后再 head.prev 直接 NPE（我亲手踩的） | 删头后判空，空了连 tail 一起清 |
| 空链表 remove | IndexOutOfBoundsException | 范围检查查两端 |
| "删中间 O(1)" | 说法有陷阱：定位节点就要 O(n) | 只有拿到节点引用本身才是 O(1) |
| 遍历用 get(i) | 每次从头走，O(n²) | 用迭代器或 for-each |

## 我项目里在哪用到

> 健身预约系统里基本不用 LinkedList——课程列表、预约列表都是"查多改少、随机访问"场景，用 ArrayList 更合适。
> 知道什么时候不用它，和知道怎么用它一样重要。

## 追问 3 个（自己先答，再对照笔记）

1. LinkedList 适合什么场景？（频繁头尾增删、队列/栈实现——Deque）
2. JDK 为什么用双向链表而不是单向？（尾插、倒查、双端队列都需要 prev）
3. ArrayList 和 LinkedList 的 get 复杂度各是多少，为什么差这么多？

## 我原来以为 vs 其实

- 原来以为：remove 三种情况（删头/删尾/删中）很复杂。
  其实：核心就两行指针重连，特殊处理只在头尾边界。
- 原来以为：代码看着对就没问题。
  其实：删唯一元素的 NPE 是断言抓出来的——边界光靠想防不住。

## 三个月后自测

> 合上笔记，能不能手写 add / node / remove 三个方法并说清删唯一元素的坑？能 → 标 done。
