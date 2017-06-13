package hipo.com.customviewexercise;

import android.app.Application;

import io.objectbox.BoxStore;

/**
 * Created by Tolga Can "tesleax" Ünal on 13/06/17
 */
public class App extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

}
