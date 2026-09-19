
import java.util.Scanner;

public class Change{
    public static void main(String[] args) {
        Scanner sc=new
Scanner(System.in);
        System.out.print("请输入商品应付金额：");
        double payNeed=sc.nextDouble();
        double payReal=0;
        for( ; ; ){
            System.out.print("请输入客户实际支付金额：");
            payReal=sc.nextDouble();
            if(payReal>=payNeed){
                break;
            }
            System.out.println("支付金额不足，请重新输入");
        }
        double change=payReal-payNeed;
        System.out.printf("应找零:%.2f 元\n",change);
        sc.close();
    }
}