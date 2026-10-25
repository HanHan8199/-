@FunctionalInterface
interface DistanceMetric {
    double calculate(double[] a, double[] b);
}

class KMeans {
    private DistanceMetric metric;

    public void setDistanceMetric(DistanceMetric metric) {
        this.metric = metric;
    }

    public double computeDistance(double[] x, double[] y) {
        if (metric == null) {
            throw new IllegalStateException("请先设置距离策略！");
        }
        return metric.calculate(x, y);
    }
}
class EuclideanDistance implements DistanceMetric {
    @Override
    public double calculate(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}

class ManhattanDistance implements DistanceMetric {
    @Override
    public double calculate(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.abs(a[i] - b[i]);
        }
        return sum;
    }
}

public class KMeansDemo {
    public static void main(String[] args) {
        KMeans kmeans = new KMeans();
        double[] p1 = {1.0, 2.0};
        double[] p2 = {4.0, 6.0};

        kmeans.setDistanceMetric(new EuclideanDistance());
        System.out.println("欧氏距离 = " + kmeans.computeDistance(p1, p2));

        kmeans.setDistanceMetric(new ManhattanDistance());
        System.out.println("曼哈顿距离 = " + kmeans.computeDistance(p1, p2));
    }
}
