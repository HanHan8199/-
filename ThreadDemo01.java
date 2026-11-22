class Point {
    double x, y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class PointDistanceRowTask implements Runnable {
    private Point[] points;
    private double[][] distanceMatrix;
    private int startRow;
    private int endRow;

    public PointDistanceRowTask(Point[] points, double[][] distanceMatrix,
                                int startRow, int endRow) {
        this.points = points;
        this.distanceMatrix = distanceMatrix;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < points.length; j++) {
                double dx = points[i].x - points[j].x;
                double dy = points[i].y - points[j].y;
                distanceMatrix[i][j] = Math.sqrt(dx * dx + dy * dy);
            }
        }
    }
}

public class ThreadDemo01 {
    public static void main(String[] args) throws InterruptedException {
        final int N = 10000;
        final int THREAD_NUM = 4;

        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            points[i] = new Point(Math.random(), Math.random());
        }

        double[][] singleMatrix = new double[N][N];
        long tSingleStart = System.currentTimeMillis();
        new PointDistanceRowTask(points, singleMatrix, 0, N).run();
        long tSingleEnd = System.currentTimeMillis();
        System.out.println("单线程耗时：" + (tSingleEnd - tSingleStart) + " ms");

        double[][] multiMatrix = new double[N][N];
        Thread[] workers = new Thread[THREAD_NUM];

        long tMultiStart = System.currentTimeMillis();
        int rowsPerThread = N / THREAD_NUM;
        int remainder = N % THREAD_NUM;

        int currentRow = 0;
        for (int k = 0; k < THREAD_NUM; k++) {
            int assignRows = rowsPerThread + (k < remainder ? 1 : 0);
            int s = currentRow;
            int e = currentRow + assignRows;
            currentRow = e;

            Runnable task = new PointDistanceRowTask(points, multiMatrix, s, e);
            workers[k] = new Thread(task, "RowWorker-" + k);
            workers[k].start();
        }

        for (Thread w : workers) {
            w.join();
        }
        long tMultiEnd = System.currentTimeMillis();
        System.out.println(THREAD_NUM + " 线程并发耗时：" + (tMultiEnd - tMultiStart) + " ms");
    }
}
