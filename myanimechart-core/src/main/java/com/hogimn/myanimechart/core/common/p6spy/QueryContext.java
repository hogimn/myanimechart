package com.hogimn.myanimechart.core.common.p6spy;

public class QueryContext {
    private static final ThreadLocal<Integer> SELECT_ROW_COUNT =
            ThreadLocal.withInitial(() -> 0);

    private static final ThreadLocal<Long> ELAPSED_TIME =
            ThreadLocal.withInitial(() -> 0L);

    public static void reset() {
        SELECT_ROW_COUNT.set(0);
        ELAPSED_TIME.set(0L);
    }

    public static void inc() {
        SELECT_ROW_COUNT.set(SELECT_ROW_COUNT.get() + 1);
    }

    public static int get() {
        return SELECT_ROW_COUNT.get();
    }

    public static void setElapsed(long nanos) {
        ELAPSED_TIME.set(nanos);
    }

    public static long getElapsed() {
        return ELAPSED_TIME.get();
    }

    public static void clear() {
        SELECT_ROW_COUNT.remove();
        ELAPSED_TIME.remove();
    }
}