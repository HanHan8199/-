public class GenericContainerDemo {

    // 1. 泛型类：T 为类型形参
    public static class KeyValuePair<K, V> {
        private K key;
        private V value;

        public KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }

        @Override
        public String toString() {
            return "[" + key + " => " + value + "]";
        }
    }

    // 2. 独立静态泛型方法：必须自己声明 <E>
    public static <E> void printArray(E[] elements) {
        System.out.print("数组内容：");
        for(E item : elements) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    // 3. 有界泛型方法：E 必须实现 Comparable
    public static <E extends Comparable<E>> E findMaximum(E a, E b) {
        return (a.compareTo(b) > 0) ? a : b;
    }

    public static void main(String [] args) {
        KeyValuePair<String, Double> pair =
                new KeyValuePair<>("张伟", 94.5);
        System.out.println("键值对："+ pair);

        printArray(new Integer[]{10, 20, 30});
        printArray(new String[]{"Java", "Python"});

        System.out.println("较大值：" + findMaximum("Apple", "Banana"));
        System.out.println("较大值："+ findMaximum(100, 50));
    }
}