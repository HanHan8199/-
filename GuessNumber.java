
import java.util.Scanner;

public class GuessNumber {
    public static void main(String[] args) {
        Scanner sc=new
Scanner(System.in);
        int total=5;
        int correctCount=0;
        System.out.println("==小学生加减乘除答题游戏==");
        for (int i = 1; i <=total; i++) {
            int op1=(int)(Math.random()*10);
            int op2=(int)(Math.random()*10);
            int rm=(int)((Math.random()*1000)%4);
            char[] opstr = {'+','-','*','/'};
            int key=0;
            switch(rm){
                case 0:
                    key=op1+op2;
                    break;
                case 1:
                    if(op1<op2){
                        int temp=op1;
                        op1=op2;
                        op2=temp;
                    }
                    key=op1-op2;
                    break;
                case 2:
                    key=op1*op2;
                    break;
                case 3:
                    if (op2==0){
                        op2=1;
                    }
                    op1=op2*((int)(Math.random()*9)+1);
                    key=op1/op2;
                    break;
                default:
                    throw new AssertionError();
            }
            System.out.print("第"+i+"题:"+op1+opstr[rm]+op2+"=");
            int userAnswer=sc.nextInt();
            if(userAnswer==key){
                System.out.println("回答正确!");
                correctCount++;
            }
            else{
                System.out.println("回答错误,正确答案:"+key);
            }
        }
        System.out.println("\n游戏结束!一共"+total+"题,答对"+correctCount+"题");
        sc.close();
    }
    
}
