import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class KMeansND {
    public static double distance(double[] p1, double[] p2) {
        double sumSq = 0.0;
        for (int d = 0; d < p1.length; d++) {
            double diff = p1[d] - p2[d];
            sumSq += diff * diff;
        }
        return Math.sqrt(sumSq);
    }
    public static double[][] initializeCentroids(double[][] points, int k) {
        int dim = points[0].length;
        double[][] centroids = new double[k][dim];
        for (int i = 0; i < k; i++) {
            for (int d = 0; d < dim; d++) {
                centroids[i][d] = points[i][d];
            }
        }
        return centroids;
    }
    public static int[] assignClusters(double[][] points, double[][] centroids) {
        int n = points.length;
        int[] clusterId = new int[n];
        for (int i = 0; i < n; i++) {
            double minDist = Double.MAX_VALUE;
            int best = 0;
            for (int c = 0; c < centroids.length; c++) {
                double dist = distance(points[i], centroids[c]);
                if (dist < minDist) {
                    minDist = dist;
                    best = c;
                }
            }
            clusterId[i] = best;
        }
        return clusterId;
    }
    public static double[][] updateCentroids(double[][] points, int[] clusterId, int k) {
        int dim = points[0].length;
        double[][] sum = new double[k][dim];
        int[] count = new int[k];

        for (int i = 0; i < points.length; i++) {
            int c = clusterId[i];
            count[c]++;
            for (int d = 0; d < dim; d++) {
                sum[c][d] += points[i][d];
            }
        }
        double[][] newCentroids = new double[k][dim];
        for (int c = 0; c < k; c++) {
            if (count[c] > 0) {
                for (int d = 0; d < dim; d++) {
                    newCentroids[c][d] = sum[c][d] / count[c];
                }
            } else {
                System.arraycopy(sum[c], 0, newCentroids[c], 0, dim);
            }
        }
        return newCentroids;
    }
    public static boolean hasConverged(double[][] oldC, double[][] newC, double tol) {
        for (int c = 0; c < oldC.length; c++) {
            double move = distance(oldC[c], newC[c]);
            if (move > tol) {
                return false;
            }
        }
        return true;
    }
    public static double[][] readPointsFromFile(String filePath) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filePath));
        List<double[]> list = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.split("\\s+");
            double[] point = new double[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                point[i] = Double.parseDouble(tokens[i]);
            }
            list.add(point);
        }
        sc.close();
        return list.toArray(new double[0][]);
    }
    public static void writeResultsToFile(String filePath, double[][] centroids, int[] clusterId)
            throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File(filePath));
        pw.println("==== 最终多维质心 ====");
        for (int i = 0; i < centroids.length; i++) {
            pw.print("Centroid " + i + " : ");
            for (double v : centroids[i]) {
                pw.printf("%.4f  ", v);
            }
            pw.println();
        }
        pw.println("\n==== 样本所属簇 ====");
        for (int i = 0; i < clusterId.length; i++) {
            pw.println("Point " + i + " -> Cluster " + clusterId[i]);
        }
        pw.close();
    }
    public static void main(String[] args) {
        try {
            int k = 3;
            double tolerance = 1e-4;
            int maxIter = 100;
            double[][] points = readPointsFromFile("input.txt");
            double[][] centroids = initializeCentroids(points, k);
            int[] clusterLabel = null;
            for (int iter = 0; iter < maxIter; iter++) {
                clusterLabel = assignClusters(points, centroids);
                double[][] newCent = updateCentroids(points, clusterLabel, k);
                if (hasConverged(centroids, newCent, tolerance)) {
                    System.out.println("收敛，迭代次数：" + (iter + 1));
                    centroids = newCent;
                    break;
                }
                centroids = newCent;
            }
            writeResultsToFile("output.txt", centroids, clusterLabel);
            System.out.println("聚类完成，结果写入 output.txt");
        } catch (FileNotFoundException e) {
            System.out.println("找不到输入文件 input.txt");
            e.printStackTrace();
        }
    }
}