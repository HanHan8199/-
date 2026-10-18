import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Point {
    private double[] coords;
    public Point(double[] arr) {
        coords = arr.clone();
    }
    public double distance(Point other) {
        double sum = 0;
        for (int i = 0; i < coords.length; i++) {
            double diff = coords[i] - other.coords[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public double[] getCoords() {
        return coords.clone();
    }
}
class KMeans {
    private int k;
    private List<Point> centroids;
    private final double eps = 1e-6;

    public KMeans(int kValue) {
        this.k = kValue;
        centroids = new ArrayList<>();
    }

    public void initCentroids(List<Point> points) {
        centroids.clear();
        for (int i = 0; i < k; i++) {
            centroids.add(points.get(i));
        }
    }

    public int[] assignCluster(List<Point> points) {
        int[] label = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            int bestIdx = 0;
            double minDist = p.distance(centroids.get(0));
            for (int c = 1; c < k; c++) {
                double d = p.distance(centroids.get(c));
                if (d < minDist) {
                    minDist = d;
                    bestIdx = c;
                }
            }
            label[i] = bestIdx;
        }
        return label;
    }

    public List<Point> updateCentroids(List<Point> points, int[] label) {
        int dim = points.get(0).getCoords().length;
        List<double[]> sumArr = new ArrayList<>();
        int[] count = new int[k];
        for (int i = 0; i < k; i++) {
            sumArr.add(new double[dim]);
        }

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            int cid = label[i];
            count[cid]++;
            double[] pc = p.getCoords();
            for (int d = 0; d < dim; d++) {
                sumArr.get(cid)[d] += pc[d];
            }
        }

        List<Point> newCent = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            double[] avg = new double[dim];
            if(count[i]==0){
                avg = sumArr.get(i);
            }else{
                for(int d=0;d<dim;d++) avg[d] = sumArr.get(i)[d]/count[i];
            }
            newCent.add(new Point(avg));
        }
        return newCent;
    }

    public boolean isConverged(List<Point> oldC, List<Point> newC) {
        for (int i = 0; i < k; i++) {
            if (oldC.get(i).distance(newC.get(i)) > eps) {
                return false;
            }
        }
        return true;
    }

    public int[] fit(List<Point> dataPoints) {
        initCentroids(dataPoints);
        List<Point> oldCent;
        int[] labels;
        while (true) {
            labels = assignCluster(dataPoints);
            List<Point> newCent = updateCentroids(dataPoints, labels);
            oldCent = centroids;
            centroids = newCent;
            if (isConverged(oldCent, newCent)) break;
        }
        return labels;
    }
}
public class KMeansOOPMain {
    public static List<Point> readPoints(String filePath) throws FileNotFoundException {
        List<Point> list = new ArrayList<>();
        Scanner sc = new Scanner(new File(filePath));
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if(line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            double[] arr = new double[parts.length];
            for(int i=0;i<arr.length;i++) arr[i] = Double.parseDouble(parts[i]);
            list.add(new Point(arr));
        }
        sc.close();
        return list;
    }
    public static void writeResult(List<Point> pts, int[] labels, String outPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
        for(int i=0;i<pts.size();i++){
            double[] c = pts.get(i).getCoords();
            for(double v:c) bw.write(v+"\t");
            bw.write("簇:"+labels[i]+"\n");
        }
        bw.close();
    }

    public static void main(String[] args) {
        try {
            List<Point> dataset = readPoints("input_points.txt");
            KMeans km = new KMeans(3); // k=3，可以修改
            int[] resLabel = km.fit(dataset);
            writeResult(dataset,resLabel,"cluster_results.txt");
            System.out.println("聚类完成，结果写入cluster_results.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
