---
type: note
topic: java
status: review
tags: [动态代理, jdk, cglib]
created: 2026-11-21
source: JdkProxyDemo 实践 / Spring AOP 铺垫
---

# JDK 动态代理 vs CGLIB

## 一句话结论

> > JDK 代理靠"实现同一接口"，CGLIB 靠"继承目标类"；共同点是中间人在调用前后做增强。这就是 Spring AOP 的原始形态。

## 原理（自己画，不要贴别人的图）



- - 代理模式：不直接调目标对象，经过中间人，调用前后塞日志/事务/权限检查
    - JDK 代理三件套：Proxy.newProxyInstance + 类加载器 + InvocationHandler
      - invoke 方法里：method.invoke(真实对象, args) 前后就是增强点
    - JDK 的命门：必须有接口——代理类和实现类是"兄弟"关系（都实现接口），不是子类
    - CGLIB：运行时生成目标类的子类，通过重写方法增强——无接口也能代理，但 final 类/方法不行
    - Spring 的选择：目标有接口默认 JDK 代理，无接口用 CGLIB

## 边界与坑

| 场景 | 会发生什么 | 怎么规避 |
|---|---|---|
| 只有实现类没有接口              | JDK 代理用不了               | 用 CGLIB               |
| proxy instanceof 实现类         | 是 false（兄弟不是子类）     | 判断类型用接口         |
| CGLIB 代理 final 类/方法        | 代理失败                     | 目标类设计时避免 final |
| InvocationHandler 忘了调 invoke | 原方法根本没执行，增强变替换 | invoke 调用是必经之路  |

## 我项目里在哪用到

>   > 健身项目里暂未用代理。但 W15 学 Spring AOP 时它会回来——事务、日志切面都是动态代理实现的。
>   >
>   > 面试口径：我手写过 JDK 代理的 demo，知道 Spring 切面背后就是 InvocationHandler 那套。

## 追问 3 个（自己先答，再对照笔记）

1. JDK 代理为什么要求接口？（代理类要实现同一组接口，没有接口就没有"共同身份"）
  2. CGLIB 为什么不能代理 final 方法？（重写是它的增强手段，final 断了这条路）
  3. 事务注解 @Transactional 是怎么和动态代理联系起来的？（AOP 切面 = 代理的增强，W15 揭晓）

## 我原来以为 vs 其实

  - 原来以为：代理类是目标类的子类。
    其实：JDK 代理下两者是兄弟关系——我 demo 里亲自验证 instanceof 为 false。
    - 原来以为：动态代理是 Spring 才有的高级技术。
      其实：核心就是 JDK 自带的一个 Proxy 类 + 一个 invoke 回调。

- 

> 合上笔记，能不能说出 JDK 与 CGLIB 的三点差异（实现方式/接口要求/final 限制），并写出 Proxy.newProxyInstance
>   的调用骨架？能 → 标 done。
