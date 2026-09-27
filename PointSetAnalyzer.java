// PointSetAnalyzer.java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PointSetAnalyzer {
    public static double[] computeCenter(double[][] ps){
        double[] center = new double[2];
        double sumX = 0, sumY = 0;
        for(int i=0;i<ps.length;i++){
            sumX += ps[i][0];
            sumY += ps[i][1];
        }
        center[0] = sumX / ps.length;
        center[1] = sumY / ps.length;
        return center;
    }
    public static String printPoints(double[][] ps){
        String str="";
        for(int i=0;i<ps.length;i++){
            // System.out.println("X:"+ps[i][0]+",Y:"+ps[i][1]);
            str+="X:"+ps[i][0]+",Y:"+ps[i][1]+"\n";
        }
        return str;
    }
    public static double[][] iniCenter(int k,String outFilePath){
        int n=20;
        double[][] points=new double[n][2];
        double minX=2000.0,maxX=0.0,minY=2000.0,maxY=0.0;
        for(int i=0;i<n;i++){
            points[i][0]=Math.random()*100+100;
            points[i][1]=Math.random()*100+100;
            if(minX>points[i][0]){
                minX=points[i][0];
            }
            if(minY>points[i][1]){
                minY=points[i][1];
            }
            if(maxX<points[i][0]){
                maxX=points[i][0];
            }
            if(maxY<points[i][1]){
                maxY=points[i][1];
            }
        }
        double[][] klist=new double[k][2];
        for(int i=0;i<k;i++){
            klist[i][0]=Math.random()*(maxX-minX)+minX;
            klist[i][1]=Math.random()*(maxY-minY)+minY;
        }
        return klist;
    }
    public static double distanceOfTwoPoints(double[] p1, double[] p2){
        double dis=0.0;
        dis=Math.sqrt(Math.pow((p1[0]-p2[0]),2)+Math.pow((p1[1]-p2[1]),2));
        return dis;
    }
    public static int getShortestOfcenter(double[] point,double[][] klist){
        int id=0;
        double shortestDis=20000.0;
        for(int i=0;i<klist.length;i++){
            double tempDis=distanceOfTwoPoints(point,klist[i]);
            if(tempDis < shortestDis){
                shortestDis = tempDis;
                id = i;
            }
        }
        return id;
    }
    public static double[][] readerPointFile(String filePath) throws IOException{
        BufferedReader reader=new BufferedReader(new FileReader(filePath));
        int count=0;
        String line="";
        while((line=reader.readLine())!=null){
            System.out.println(line);
            count++;
        }
        reader.close();
        BufferedReader reader2=new BufferedReader(new FileReader(filePath));
        double[][] points = new double[count][2];
        int idx=0;
        while((line=reader2.readLine())!=null){
            String[] parts = line.split(",");
            points[idx][0] = Double.parseDouble(parts[0].trim());
            points[idx][1] = Double.parseDouble(parts[1].trim());
            idx++;
        }
        reader2.close();
        return points;
    }
    public static double[] computeAllPointsDisofEach(double[][] ps){
        int edgeN=(int)(ps.length*(ps.length-1)/2);
        double[] disList=new double[edgeN];
        return disList;
    }
    public static double[][] kMeans(double[][] points, int k, int maxIter){
        double[][] centers = iniCenter(k,null);
        boolean isChange;
        do {
            isChange = false;
            double[][] oldCenter = new double[k][2];
            for(int i=0;i<k;i++){
                oldCenter[i][0] = centers[i][0];
                oldCenter[i][1] = centers[i][1];
            }
            int[] clusterCount = new int[k];
            double[][] sumP = new double[k][2];
            for(int p=0;p<points.length;p++){
                int cid = getShortestOfcenter(points[p],centers);
                sumP[cid][0] += points[p][0];
                sumP[cid][1] += points[p][1];
                clusterCount[cid]++;
            }
            for(int c=0;c<k;c++){
                if(clusterCount[c]>0){
                    centers[c][0] = sumP[c][0]/clusterCount[c];
                    centers[c][1] = sumP[c][1]/clusterCount[c];
                }
                if(Math.abs(centers[c][0]-oldCenter[c][0])>1e-6 || Math.abs(centers[c][1]-oldCenter[c][1])>1e-6){
                    isChange = true;
                }
            }
            maxIter--;
        }while(isChange && maxIter>0);
        return centers;
    }
    public static void main(String[] args) {
        int n=20;
        double[][] points=new double[n][2];
        for(int i=0;i<n;i++){
            points[i][0]=Math.random();
            points[i][1]=Math.random();
        }
        System.out.print(printPoints(points));
        double[] ct = computeCenter(points);
        System.out.print("几何中心:"+ct[0]+","+ct[1]);
    }
}