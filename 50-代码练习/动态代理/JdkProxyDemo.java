import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 1. 先定义接口（JDK代理强制要求）
interface UserService {
    void register();
}

// 2. 目标类：真实业务，只写核心逻辑，不加日志
class UserServiceImpl implements UserService {
    @Override
    public void register() {
        System.out.println("【业务】用户注册逻辑执行");
    }
}

// 3. 调用处理器：写增强逻辑（日志/事务）
class LogHandler implements InvocationHandler {
    // 持有真实目标对象
    private final Object target;
    public LogHandler(Object target) {
        this.target = target;
    }

    // 代理对象调用方法，都会进入invoke
    // proxy：代理对象；method：被调用的方法；args：方法参数
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // ========== 前置增强 ==========
        System.out.println("【前置】日志：准备调用方法 " + method.getName());

        // 执行原始目标方法
        Object result = method.invoke(target, args);

        // ========== 后置增强 ==========
        System.out.println("【后置】日志：方法调用结束");
        return result;
    }
}

// 测试
public class JdkProxyDemo {
    public static void main(String[] args) {
        // 真实对象
        UserService target = new UserServiceImpl();
        // 处理器
        InvocationHandler handler = new LogHandler(target);

        // 动态生成代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),    // 类加载器
                target.getClass().getInterfaces(),      // 目标实现的接口数组
                handler
        );

        // 调用代理的方法，不是直接调用target！
        proxy.register();
    }
}
