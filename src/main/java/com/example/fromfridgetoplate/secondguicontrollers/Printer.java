package com.example.fromfridgetoplate.secondguicontrollers;

public class Printer {
    private Printer(){
        throw new IllegalStateException();
    }
    public static void print(String s){
        System.out.println(s);
    }
}
