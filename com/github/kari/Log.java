package com.github.kari;

public final class Log {
    public static void d(String msg) {
        System.out.println(msg);
    }

    public static void f(String format, Object... args) {
        System.out.printf(format, args);
    }
}