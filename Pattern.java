public class Pattern{
    public static void main(String[] args) {
        System.out.println("==题目1.3==");
        printPattern();//第一题
        System.out.println("\n==题目1.4==");
        printTable();//第二题
        System.out.println("\n==题目1.7==");
        double Pi1=computePi(11);
        double pi2=computePi(13);
        System.out.println("4*(1-1/3+1/5-1/7+1/9-1/11)="+Pi1);
        System.out.println("4*(1-1/3+1/5-1/7+1/9-1/11+1/13)="+pi2);//第三题
    }
    //题目1.3
    public static void printPattern(){
        for(int line=0;line<4;line++){//控制行数
            for(int space=0;space<3-line;space++){//打印前面空格
                System.out.print(" ");
            }
            if (line==0){
                System.out.println("   J    A    V     V    A");}
            else if (line==1){
                System.out.println("   J   A A    V   V    A A");}
            else if (line==2){
                System.out.println("J  J  AAAAA    V V    AAAAA");}
            else if (line==3){
                System.out.println(" JJ  A     A    V    A     A");}
                }
            }
    //题目1.4
    public static void printTable(){
        System.out.println("a    a^2    a^3");
        for(int a=1;a<=4;a++){
            int square=a*a;
            int cube=a*a*a;
            System.out.print(a+"    ");
            System.out.print(square+"    ");
            System.out.println(cube);
        }
    }
    //题目1.7
    public static double computePi(int n){
        double sumPi=0.0;
        int j=0;
        for(int i=1;i<=n;i=i+2){
            double single=1.0/(double)i;
            single=single*Math.pow(-1,j);
            j++;
            sumPi=sumPi+single;
        }
        return sumPi*4;

    }
        }
