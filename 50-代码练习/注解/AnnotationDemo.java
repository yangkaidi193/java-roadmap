import java.lang.annotation.*;
import java.lang.reflect.Field;

// ===== 1. 自定义 @MyAutowired：空标记注解 =====
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)   // 自测①把它改成 SOURCE 再跑
@interface MyAutowired {
}

// ===== 自测③：带默认值元素的注解 =====
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface MyValue {
    String value() default "默认值";
    int order() default 1;
}

class AnnotationUserService {
    @MyAutowired
    private Object dependency;

    @MyAutowired
    private String anotherDep;

    @MyValue(order = 2)               // 只覆盖 order，value 用默认值
    private String config;
}

public class AnnotationDemo {
    public static void main(String[] args) {
        Class<?> clazz = AnnotationUserService.class;

        for (Field f : clazz.getDeclaredFields()) {
            // 读 @MyAutowired
            if (f.isAnnotationPresent(MyAutowired.class)) {
                System.out.println("找到需要注入的字段: " + f.getName()
                        + "  类型: " + f.getType().getSimpleName());
            }
            // 读 @MyValue
            MyValue mv = f.getAnnotation(MyValue.class);
            if (mv != null) {
                System.out.println("MyValue -> value=" + mv.value()
                        + ", order=" + mv.order());
            }
        }
    }
}