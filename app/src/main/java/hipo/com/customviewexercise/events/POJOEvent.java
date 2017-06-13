package hipo.com.customviewexercise.events;

/**
 * Created by Tolga Can "tesleax" Ünal on 13/06/17
 */

public class POJOEvent {
    private final String message;

    public POJOEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
