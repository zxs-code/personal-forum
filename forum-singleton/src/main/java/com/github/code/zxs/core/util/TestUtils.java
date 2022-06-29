package com.github.code.zxs.core.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUtils {
    /**
     * 计算方法所用时间，不是cpu占用时间
     *
     * @param function
     * @return
     */
    public static Duration costTime(int loop, NoArgumentAndReturnFunction function) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++)
            function.apply();
        long end = System.currentTimeMillis();
        return Duration.of(end - start, ChronoUnit.MILLIS);
    }

    /**
     * 计算方法所用时间，不是cpu占用时间
     *
     * @param function
     * @return
     */
    public static void costTimePrintConcurrent(int loop, NoArgumentAndReturnFunction function) {
        System.out.println(costTimeConcurrent(loop, function).toMillis() + "ms");
    }

    /**
     * 计算方法所用时间，不是cpu占用时间
     *
     * @param function
     * @return
     */
    public static Duration costTimeConcurrent(int loop, NoArgumentAndReturnFunction function) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++)
            executorService.execute(function::apply);
        long end = System.currentTimeMillis();
        return Duration.of(end - start, ChronoUnit.MILLIS);
    }

    /**
     * 计算方法所用时间，不是cpu占用时间
     *
     * @param function
     * @return
     */
    public static void costTimePrint(int loop, NoArgumentAndReturnFunction function) {
        System.out.println(costTime(loop, function).toMillis() + "ms");
    }

    public static Duration costTime(int loop, NoArgumentFunction function) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++)
            function.apply();
        long end = System.currentTimeMillis();
        return Duration.of(end - start, ChronoUnit.MILLIS);
    }

    public static <A1> Duration costTime(int loop, OneArgumentFunction<A1> function, A1[] arg1) {
        ArrayUtils.requireNonEmpty(arg1);
        int index = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            function.apply(arg1[index++]);
            if (index == arg1.length)
                index = 0;
        }
        long end = System.currentTimeMillis();
        return Duration.of(end - start, ChronoUnit.MILLIS);
    }

    public static <A1, R> Duration costTime(int loop, OneArgumentReturnFunction<A1, R> function, A1[] arg1) {
        ArrayUtils.requireNonEmpty(arg1);
        int index = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            function.apply(arg1[index++]);
            if (index == arg1.length)
                index = 0;
        }
        long end = System.currentTimeMillis();
        return Duration.of(end - start, ChronoUnit.MILLIS);
    }


    public interface NoArgumentAndReturnFunction {
        void apply();
    }

    public interface NoArgumentFunction<R> {
        R apply();
    }

    public interface OneArgumentFunction<A1> {
        void apply(A1 arg1);
    }

    public interface OneArgumentReturnFunction<A1, R> {
        R apply(A1 arg1);
    }

    public interface TwoArgumentFunction<A1, A2> {
        void apply(A1 arg1, A2 arg2);
    }

    public interface TwoArgumentReturnFunction<A1, A2, R> {
        R apply(A1 arg1, A2 arg2);
    }

    public interface ThreeArgumentFunction<A1, A2, A3> {
        void apply(A1 arg1, A2 arg2, A3 arg3);
    }

    public interface ThreeArgumentReturnFunction<A1, A2, A3, R> {
        R apply(A1 arg1, A2 arg2, A3 arg3);
    }
}
