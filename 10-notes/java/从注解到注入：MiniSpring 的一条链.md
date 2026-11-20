---
type: note
topic: java
status: review
tags: [反射, 注解, 依赖注入]
created: 2026-11-20
source: 手写 MiniSpring 实践
---

# 从注解到注入：MiniSpring 的一条链

## 一句话结论

> > 注解只是标记，反射是翻墙的手，容器把它们串成注入链：查缓存 → 反射创建 → 扫 @MyAutowired → 递归注入 → 存缓存。

## 原理（自己画，不要贴别人的图）



- - 第一步：自定义注解标 @Retention(RUNTIME)——不标 RUNTIME 反射就读不到，注入无从谈起
    - 第二步：反射翻墙——getDeclaredField 拿 private 字段 + setAccessible(true) 绕过访问检查
    - 第三步：容器组装——递归 getBean 创建依赖、单例缓存防重复、注入后返回完整对象
    - 三个零件缺一不可：没有注解就没有标记，没有反射就没有能力，没有容器就没有组织）

## 边界与坑

| 场景 | 会发生什么 | 怎么规避 |
|---|---|---|
| @Retention 改 SOURCE       | 运行期反射读不到注解，注入静默失效  | 需要运行期读的注解必须 RUNTIME                        |
| 循环依赖（A依赖B，B依赖A） | 无限递归 StackOverflow              | 缓存前先放"半成品"（Spring 三级缓存的思路，W15 揭晓） |
| 类没有无参构造             | 反射创建时抛 InstantiationException | 容器只管理有无参构造的 Bean                           |
| setAccessible 滥用         | 可以改 JDK 内部状态，有安全风险     | 生产代码里慎重，框架内部才用                          |

## 我项目里在哪用到

>   > 健身项目的 Controller 里 @Autowired 注入 Service——Spring 做的就是 MiniSpring 放大一万倍的版本。
>   >
>   > 面试讲这个：我手写过 50 行 mini 版，所以知道 Spring 注入背后是什么在干活。

## 追问 3 个（自己先答，再对照笔记）

1. 自定义注解为什么要标 RUNTIME 而不是 SOURCE？
  2. setAccessible(true) 绕过的是什么？为什么不绕过会失败？
  3. 循环依赖为什么会导致无限递归？有什么解法思路？

## 我原来以为 vs 其实

  - 原来以为：@Autowired 是 Spring 的黑魔法。
    其实：自己 50 行就能写出 mini 版——标记、反射、容器三件事。
  - 原来以为：注解本身会做事情。
    其实：注解只是个标签，所有"事情"都是反射在干。



- 

  

> 合上笔记，能不能默写 MiniSpring 的五步注入流程并讲清循环依赖问题？能 → 标 done。
