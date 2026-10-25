import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
interface DistanceMetric {
    double calculate(double[] a, double[] b);
}

class Cluster {
    List<Integer> points;

    public Cluster(int singlePointIndex) {
        points = new ArrayList<>();
        points.add(singlePointIndex);
    }

    public Cluster(Cluster c1, Cluster c2) {
        points = new ArrayList<>(c1.points);
        points.addAll(c2.points);
    }

    public int size() {
        return points.size();
    }
}

class HierarchicalCluster {
    private final double[][] data;
    private DistanceMetric metric;

    public HierarchicalCluster(double[][] data) {
        this.data = data;
    }

    public void setDistanceMetric(DistanceMetric metric) {
        this.metric = metric;
    }

    private double clusterDistance(Cluster ca, Cluster cb) {
        double sum = 0;
        int count = 0;
        for (int i : ca.points) {
            for (int j : cb.points) {
                sum += metric.calculate(data[i], data[j]);
                count++;
            }
        }
        return sum / count;
    }

    public List<Cluster> fit(int targetNum) {
        List<Cluster> clusterList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            clusterList.add(new Cluster(i));
        }

        while (clusterList.size() > targetNum) {
            double minDist = Double.MAX_VALUE;
            int mergeA = 0, mergeB = 1;

            for (int i = 0; i < clusterList.size(); i++) {
                for (int j = i + 1; j < clusterList.size(); j++) {
                    double d = clusterDistance(clusterList.get(i), clusterList.get(j));
                    if (d < minDist) {
                        minDist = d;
                        mergeA = i;
                        mergeB = j;
                    }
                }
            }

            Cluster c1 = clusterList.get(mergeA);
            Cluster c2 = clusterList.get(mergeB);
            Cluster newCluster = new Cluster(c1, c2);

            if (mergeB > mergeA) {
                clusterList.remove(mergeB);
                clusterList.remove(mergeA);
            } else {
                clusterList.remove(mergeA);
                clusterList.remove(mergeB);
            }
            clusterList.add(newCluster);
        }
        return clusterList;
    }
}

public class HierarchicalClusteringDemo {
    public static void main(String[] args) {
        double[][] dataset = {
                {1.0, 2.0},
                {1.2, 2.1},
                {4.0, 5.0},
                {4.2, 5.1},
                {8.0, 9.0}
        };

        DistanceMetric euclid = (a, b) -> {
            double sum = 0;
            for (int i = 0; i < a.length; i++) {
                double diff = a[i] - b[i];
                sum += diff * diff;
            }
            return Math.sqrt(sum);
        };

        HierarchicalCluster hc = new HierarchicalCluster(dataset);
        hc.setDistanceMetric(euclid);

        int targetClusterCount = 2;
        List<Cluster> result = hc.fit(targetClusterCount);

        System.out.println("==== 层次聚类（凝聚式）结果 ====");
        System.out.println("目标簇数量：" + targetClusterCount);
        for (int i = 0; i < result.size(); i++) {
            Cluster cl = result.get(i);
            System.out.printf("簇 %d，样本下标列表：%s%n", i, cl.points);
        }
    }
}
