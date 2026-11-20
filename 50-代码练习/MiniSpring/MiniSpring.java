import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

// ===== 注解（周三的零件，RUNTIME 才能在反射里读到）=====
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface MyInject {}

// ===== 演示类 1：两层依赖 Service -> Repository =====
class UserRepository {
    public void save() {
        System.out.println("数据存好了");
    }
}

class SpringUserService {
    @MyInject
    private UserRepository repository;   // 由框架注入，不 new

    public void register() {
        repository.save();
    }
}

// ===== 演示类 2：三层依赖 Service -> Repository -> Dao =====
class OrderDao {
    public void save() {
        System.out.println("OrderDao: 订单落库");
    }
}

class OrderRepository {
    @MyInject
    private OrderDao dao;

    public void save() {
        dao.save();
    }
}

class OrderService {
    @MyInject
    private OrderRepository repo;

    public void create() {
        repo.save();
    }
}

// ===== 核心：MiniSpring =====
public class MiniSpring {
    private final Map<Class<?>, Object> beans = new HashMap<>();   // 单例缓存

    public <T> T getBean(Class<T> clazz) {
        // 第1步：缓存命中直接返回
        Object cached = beans.get(clazz);
        if (cached != null) {
            return clazz.cast(cached);
        }

        try {
            // 第2步：反射创建实例
            T instance = clazz.getDeclaredConstructor().newInstance();

            // 第3步：遍历字段找 @MyInject
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(MyInject.class)) {
                    // 第4步：递归获取依赖 + 注入
                    Object dependency = getBean(field.getType());
                    field.setAccessible(true);   // private 字段翻墙
                    field.set(instance, dependency);
                }
            }

            // 第5步：放入缓存并返回
            beans.put(clazz, instance);
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("创建 bean 失败: " + clazz.getName(), e);
        }
    }

    // ===== 验证 =====
    public static void main(String[] args) {
        MiniSpring ctx = new MiniSpring();

        System.out.println("--- 验证1：注入发生（repository 不是 null）---");
        SpringUserService svc = ctx.getBean(SpringUserService.class);
        svc.register();                              // 数据存好了

        System.out.println("--- 验证2：单例（两次拿到同一实例）---");
        SpringUserService svc2 = ctx.getBean(SpringUserService.class);
        System.out.println("svc == svc2 ? " + (svc == svc2));   // true

        System.out.println("--- 验证3：递归注入（Service→Repository→Dao）---");
        OrderService os = ctx.getBean(OrderService.class);
        os.create();                                 // OrderDao: 订单落库
    }
}
