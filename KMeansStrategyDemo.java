import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
interface DistanceCalculator {
    double calc(Point p1, Point p2);
}
class Point {
    private double[] coords;

    public Point(double[] arr) {
        coords = arr.clone();
    }

    public double[] getCoords() {
        return coords.clone();
    }
}
class EuclideanDistance implements DistanceCalculator {
    @Override
    public double calc(Point p1, Point p2) {
        double sum = 0;
        double[] a1 = p1.getCoords();
        double[] a2 = p2.getCoords();
        for (int i = 0; i < a1.length; i++) {
            double diff = a1[i] - a2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}

class ManhattanDistance implements DistanceCalculator {
    @Override
    public double calc(Point p1, Point p2) {
        double sum = 0;
        double[] a1 = p1.getCoords();
        double[] a2 = p2.getCoords();
        for (int i = 0; i < a1.length; i++) {
            sum += Math.abs(a1[i] - a2[i]);
        }
        return sum;
    }
}

class KMeans {
    private int k;
    private List<Point> centroids;
    private final double eps = 1e-6;
    private DistanceCalculator distanceCalculator;

    public KMeans(int kValue, DistanceCalculator calculator) {
        this.k = kValue;
        this.distanceCalculator = calculator;
        centroids = new ArrayList<>();
    }

    private void initCentroids(List<Point> dataPoints) {
        centroids.clear();
        Random rand = new Random();
        int n = dataPoints.size();
        boolean[] selected = new boolean[n];
        for (int i = 0; i < k; ) {
            int idx = rand.nextInt(n);
            if (!selected[idx]) {
                selected[idx] = true;
                centroids.add(dataPoints.get(idx));
                i++;
            }
        }
    }

    public int[] assignCluster(List<Point> dataPoints) {
        int[] labels = new int[dataPoints.size()];
        for (int i = 0; i < dataPoints.size(); i++) {
            Point p = dataPoints.get(i);
            double minDist = Double.MAX_VALUE;
            int clusterId = 0;
            for (int c = 0; c < centroids.size(); c++) {
                double dist = distanceCalculator.calc(p, centroids.get(c));
                if (dist < minDist) {
                    minDist = dist;
                    clusterId = c;
                }
            }
            labels[i] = clusterId;
        }
        return labels;
    }

    public List<Point> updateCentroids(List<Point> dataPoints, int[] labels) {
        int dim = dataPoints.get(0).getCoords().length;
        List<double[]> sumArr = new ArrayList<>();
        int[] count = new int[k];
        for (int i = 0; i < k; i++) {
            sumArr.add(new double[dim]);
        }
        for (int i = 0; i < dataPoints.size(); i++) {
            int cid = labels[i];
            double[] pc = dataPoints.get(i).getCoords();
            double[] sc = sumArr.get(cid);
            for (int d = 0; d < dim; d++) {
                sc[d] += pc[d];
            }
            count[cid]++;
        }
        List<Point> newCent = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            double[] avg = new double[dim];
            double[] s = sumArr.get(i);
            for (int d = 0; d < dim; d++) {
                avg[d] = s[d] / count[i];
            }
            newCent.add(new Point(avg));
        }
        return newCent;
    }

    public boolean isConverged(List<Point> oldC, List<Point> newC) {
        for (int i = 0; i < oldC.size(); i++) {
            double d = distanceCalculator.calc(oldC.get(i), newC.get(i));
            if (d > eps) {
                return false;
            }
        }
        return true;
    }

    public int[] fit(List<Point> dataPoints) {
        initCentroids(dataPoints);
        List<Point> oldCent;
        List<Point> newCent;
        int[] labels;
        while (true) {
            labels = assignCluster(dataPoints);
            newCent = updateCentroids(dataPoints, labels);
            oldCent = centroids;
            centroids = newCent;
            if (isConverged(oldCent, newCent)) {
                break;
            }
        }
        return labels;
    }

public List<Point> getCentroids() {
        return centroids;
    }
}
public class KMeansStrategyDemo {
    public static List<Point> readPoints(String filePath) throws FileNotFoundException {
        List<Point> list = new ArrayList<>();
        Scanner sc = new Scanner(new File(filePath));
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            double[] arr = new double[parts.length];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = Double.parseDouble(parts[i]);
            }
            list.add(new Point(arr));
        }
        sc.close();
        return list;
    }
    public static void writeResultToFile(String filename, List<Point> centroids, int[] labels, List<Point> data) throws IOException {
        FileWriter fw = new FileWriter(filename);
        fw.write("===== 最终中心点 =====\n");
        for (int i = 0; i < centroids.size(); i++) {
            double[] co = centroids.get(i).getCoords();
            fw.write("Centroid " + i + " : ");
            for (double v : co) fw.write(v + " ");
            fw.write("\n");
        }
        fw.write("\n===== 每个点对应的簇标签 =====\n");
        for (int i = 0; i < labels.length; i++) {
            double[] co = data.get(i).getCoords();
            for (double v : co) fw.write(v + " ");
            fw.write(" → cluster:" + labels[i] + "\n");
        }
        fw.close();
    }
    public static void main(String[] args) {
        try {
            List<Point> dataPoints = readPoints("input.txt");
            DistanceCalculator calc = new EuclideanDistance();
            KMeans km = new KMeans(3, calc);
            int[] resultLabels = km.fit(dataPoints);
            List<Point> finalCent = km.getCentroids();
            writeResultToFile("output.txt", finalCent, resultLabels, dataPoints);
            System.out.println("聚类完成，结果写入 output.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}