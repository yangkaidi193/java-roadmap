import java.util.*;
import java.util.stream.*;

public class StreamLambdaDemo {
    public static void main(String[] args) {
        List<Course> courses = Arrays.asList(
                new Course("瑜伽入门", "初级", 8),
                new Course("动感单车", "中级", 15),
                new Course("普拉提", "高级", 3),
                new Course("燃脂操", "初级", 20),
                new Course("拳击", "中级", 0),
                new Course("游泳", "高级", 6),
                new Course("舞蹈基础", "高级", 0),
                new Course("HIIT", "高级", 12),
                new Course("太极", "高级", 5),
                new Course("攀岩", "高级", 9)
        );
        Map<String, Long> levelCount = courses.stream()
                .collect(Collectors.groupingBy(Course::getLevel, Collectors.counting()));
        System.out.println("各难度课程数：" + levelCount);

        Map<String,Integer> levelTotal =courses.stream()
                .collect(Collectors.groupingBy(Course::getLevel, Collectors.summingInt(Course::getBookedCount)));
        System.out.println("各难度课程报名总数：" + levelTotal);

        List<Course> hot = courses.stream()
                .filter(c -> c.getBookedCount() > 0)
                .sorted(Comparator.comparingInt(Course::getBookedCount).reversed())
                .collect(Collectors.toList());
        System.out.println("有预约的课程（按预约数降序）：" + hot);

        Course top =courses.stream()
                .max(Comparator.comparingInt(Course::getBookedCount))
                .orElse(null);
        System.out.println("预约数最多的课程：" + top);
    }

}
