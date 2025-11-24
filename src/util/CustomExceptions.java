package util;

public class CustomExceptions {
    public static class InvalidWorkoutException extends Exception {
        public InvalidWorkoutException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }
}
