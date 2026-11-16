import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GenericEraseProof {
    public static void main(String[] args) throws Exception {
        List<String> stringList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();

        // ========== 验证1：运行时Class对象完全相同 ==========
        Class<?> stringCls = stringList.getClass();
        Class<?> integerCls = integerList.getClass();

        System.out.println("两个List的Class是否相等：" + (stringCls == integerCls));
        System.out.println("运行时真实类名：" + stringCls.getName());
        System.out.println("是否带泛型信息：" + stringCls.getTypeParameters().length); // 0，运行时无泛型参数

        // ========== 验证2：反射绕开编译检查，往String集合塞Integer ==========
        stringList.add("正常字符串");

        // 反射拿到add方法，参数是擦除后的Object
        Method addMethod = stringCls.getMethod("add", Object.class);
        addMethod.invoke(stringList, 12345); // 运行成功，不会报错
        addMethod.invoke(stringList, 3.14);  // 运行成功

        System.out.println("\nList最终内容：" + stringList);
        System.out.println("元素0的真实类型：" + stringList.get(0).getClass());
        System.out.println("元素1的真实类型：" + stringList.get(1).getClass());

        // 注意：如果写 String s = stringList.get(1); 运行会报ClassCastException
        // 因为编译器自动插入了(String)强转，而实际元素是Integer
    }
}
