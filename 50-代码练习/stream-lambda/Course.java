public class Course {
    private String name;  //课程名
    private String level;  //初级/中级/高级
    private int bookedCount;  //已报名人数

    public Course(String name, String level, int bookedCount) {
        this.name = name;
        this.level = level;
        this.bookedCount = bookedCount;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public String toString() {
        return name +"("+level+"已约"+bookedCount+")";
    }

}
