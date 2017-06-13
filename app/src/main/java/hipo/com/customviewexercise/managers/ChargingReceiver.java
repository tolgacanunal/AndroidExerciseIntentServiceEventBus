package hipo.com.customviewexercise.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;

import org.greenrobot.eventbus.EventBus;

import hipo.com.customviewexercise.ChargingEvent;

/**
 * Created by Tolga Can "tesleax" Ünal on 13/06/17
 */
public class ChargingReceiver extends BroadcastReceiver{
    private EventBus bus = EventBus.getDefault();

    // creating receiver for power connected or disconnected.
    @Override
    public void onReceive(Context context, Intent intent) {
        ChargingEvent event = null;

        Time now = new Time();
        now.setToNow();
        String timeOfEvent = now.format("%H:%M:%S");

        String eventData = "@" + timeOfEvent + "this device started ";
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            event = new ChargingEvent(eventData + "charging");
        } else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            event = new ChargingEvent(eventData + "discharging");
        }

        bus.post(event);
    }
}
