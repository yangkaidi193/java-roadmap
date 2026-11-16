import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;   // 顶部加这个


public class PECSTest {
    public static void main(String[] args) {
        // ===================== ? extends Number 上界通配符 =====================
        List<? extends Number> extendsList = new ArrayList<Integer>(Arrays.asList(1, 2, 3));

        // 【读操作：全部合法】
        Number number = extendsList.get(0);  // OK，至少是Number
        Object object = extendsList.get(0);  // OK，Object是所有类父类
        // String s = extendsList.get(0);   // 编译报错，类型不匹配

        // 【写操作：只有null合法】
        // extendsList.add(100);             // 编译报错，不能加Integer
        // extendsList.add(3.14);            // 编译报错，不能加Double
        extendsList.add(null);               // 唯一合法写入：null

        // ===================== ? super Integer 下界通配符 =====================
        List<? super Integer> superList = new ArrayList<Number>();

        // 【写操作：Integer及其子类合法】
        superList.add(100);                  // OK，Integer本身
        superList.add(200);                  // OK
        // superList.add(3.14);              // 编译报错，Double不是Integer子类
        // superList.add(new Object());      // 编译报错

        // 【读操作：只能用Object接收】
        Object ele = superList.get(0);       // OK
        // Integer i = superList.get(0);    // 编译报错
        // Number n = superList.get(0);     // 编译报错
    }
}
