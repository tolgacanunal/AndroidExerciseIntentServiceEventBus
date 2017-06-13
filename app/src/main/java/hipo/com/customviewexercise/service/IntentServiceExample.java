package hipo.com.customviewexercise.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.greenrobot.eventbus.EventBus;

import hipo.com.customviewexercise.MainActivity;
import hipo.com.customviewexercise.events.POJOEvent;

/**
 * Created by Tolga Can "tesleax" Ünal on 13/06/17
 */
public class IntentServiceExample extends IntentService {
    public static final String SENDED_DATA = "localbroadcastmanager";

    // intent service must use default constructor with no input.
    public IntentServiceExample() {
        super("IntentServiceExample");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // event bus created and data sended.
        EventBus.getDefault().post(new POJOEvent("event bus posted"));

        // local broadcast manager created and data sent via intent
        Intent sendIntent = new Intent(MainActivity.MainActivityBroadcastReceiver.ACTION);
        sendIntent.putExtra(SENDED_DATA, "broadcast sended.");
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendIntent);
    }
}
