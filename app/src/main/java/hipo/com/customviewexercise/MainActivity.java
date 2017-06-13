package hipo.com.customviewexercise;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hipo.com.customviewexercise.events.ChargingEvent;
import hipo.com.customviewexercise.events.POJOEvent;
import hipo.com.customviewexercise.service.IntentServiceExample;
import hipo.com.customviewexercise.service.MyJobService;

public class MainActivity extends AppCompatActivity {
    private MainActivityBroadcastReceiver broadcastReceiver;
    private EventBus myEventBus = EventBus.getDefault();
    private static final int READ_CONTACTS_REQUEST_CODE = 225;
    private FirebaseJobDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checking for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS_REQUEST_CODE);
        } else {
            Toast.makeText(this, "already given", Toast.LENGTH_SHORT).show();
        }

        // FireBase job dispatcher
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyJobService.class)
                // uniquely identifies the job
                .setTag("my-unique-tag")
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 60))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_UNMETERED_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_CHARGING
                )
                .build();
        dispatcher.mustSchedule(myJob);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // it has read contacts permission.
                    Toast.makeText(this, "READ CONTACT PERMISSION GRANTED",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied. disable functionality.
                    Toast.makeText(this, "READ CONTACT PERMISSION NOT GRANTED",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        myEventBus.unregister(this);
        dispatcher.cancelAll();
        super.onDestroy();
    }

    // eventBus subscriber
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChargingEvent event) {
        Toast.makeText(this, event.getData(), Toast.LENGTH_SHORT).show();
    }

    // eventBus subscriber
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(POJOEvent pojo) {
        Toast.makeText(this, pojo.getMessage(), Toast.LENGTH_SHORT).show();
    }

    // Local Broadcast Receiver
    public class MainActivityBroadcastReceiver extends BroadcastReceiver {
        public static final String ACTION = "hipo.com.customviewexercise";

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(IntentServiceExample.SENDED_DATA);
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }
}
