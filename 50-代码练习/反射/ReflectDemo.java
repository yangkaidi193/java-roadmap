import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Person {
    private String name = "张三";
    private int age = 20;
    private void secret() {
        System.out.println("私有方法被调用了: " + name);
    }
}

public class ReflectDemo {
    public static void main(String[] args) throws Exception {
        Person p = new Person();

        // 三种任选其一
        // Class<?> clazz = Person.class;
        // Class<?> clazz = Class.forName("Person");
        Class<?> clazz = p.getClass();

        // 反射私有字段 name
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(p, "李四");
        System.out.println(nameField.get(p));

        // 反射私有方法 secret
        Method m = clazz.getDeclaredMethod("secret");
        m.setAccessible(true);
        m.invoke(p);
    }
}
