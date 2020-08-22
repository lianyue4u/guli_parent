package com.atguigu.demo;


public class Digui {
    public static void main(String[] args) {
//        1,1,2,3,5,8,13..... 递归算法
//        useGeneratedKeys
        for(int i=1;i<=5;i++){
            System.out.println(fun(i));
        }
    }

    public static int fun(int n){
        if(n<=2){
            return 1;
        }else{
            //若n=4,结果为fun（3）+fun（2）
            return fun(n-1)+fun(n-2);
        }

    }
}
