package service;

import util.ThreadUtil;

public class ActivityLogger {

    public void logActivity(String activity) {
        ThreadUtil.runAsync(() -> {
            // Simulate logging to a file or database
            System.out.println("[LOG] " + activity + " (Logged at " + new java.util.Date() + ")");
            // In a real app, this would insert into a 'logs' table
        });
    }
}
