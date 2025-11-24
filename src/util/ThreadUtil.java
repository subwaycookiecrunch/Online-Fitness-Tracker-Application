package util;

public class ThreadUtil {
    public static void runAsync(Runnable r) {
        new Thread(r).start();
    }
}
