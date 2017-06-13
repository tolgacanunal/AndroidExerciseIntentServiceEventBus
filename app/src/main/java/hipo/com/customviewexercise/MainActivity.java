package hipo.com.customviewexercise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hipo.com.customviewexercise.service.IntentServiceExample;

public class MainActivity extends AppCompatActivity {
    private MainActivityBroadcastReceiver broadcastReceiver;
    private EventBus myEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // registering event bus
        myEventBus.register(this);

        // registering local broadcast manager
        broadcastReceiver = new MainActivityBroadcastReceiver();
        IntentFilter filter = new IntentFilter(MainActivityBroadcastReceiver.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);

        // both require
        Intent intent = new Intent(this, IntentServiceExample.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        myEventBus.unregister(this);
        super.onDestroy();
    }

    // eventBus subscriber
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChargingEvent event) {
        Toast.makeText(this, event.getData(), Toast.LENGTH_SHORT).show();
    }

    // eventBus subscriber
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(POJO pojo) {
        Toast.makeText(this, pojo.getMessage(), Toast.LENGTH_SHORT).show();
    }

    // LOCAL BROADCAST RECEIVER
    public class MainActivityBroadcastReceiver extends BroadcastReceiver {
        public static final String ACTION = "hipo.com.customviewexercise";

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(IntentServiceExample.SENDED_DATA);
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }
}
