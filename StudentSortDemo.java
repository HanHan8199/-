import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Student implements Comparable<Student> {
    String name;
    double score;
    double height;

    public Student(String name, double score, double height) {
        this.name = name;
        this.score = score;
        this.height = height;
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(this.score, other.score);
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', score=" + score + ", height=" + height + "}";
    }
}

public class StudentSortDemo {
    public static void main(String[] args) {
        List<Student> stuList = new ArrayList<>();
        stuList.add(new Student("张三", 82, 175));
        stuList.add(new Student("李四", 91, 168));
        stuList.add(new Student("王五", 76, 182));

        List<Student> list1 = new ArrayList<>(stuList);
        Collections.sort(list1);
        System.out.println("==== Comparable：按成绩升序（内部自然排序）====");
        list1.forEach(System.out::println);

        List<Student> list2 = new ArrayList<>(stuList);
        Collections.sort(list2, Comparator.comparingDouble(s -> s.height));
        System.out.println("\n==== Comparator：按身高升序（外部策略）====");
        list2.forEach(System.out::println);

        List<Student> list3 = new ArrayList<>(stuList);
        Collections.sort(list3, (s1, s2) -> Double.compare(s2.score, s1.score));
        System.out.println("\n==== Comparator：按成绩降序（外部策略）====");
        list3.forEach(System.out::println);
    }
}

