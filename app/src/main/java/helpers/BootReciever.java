package helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rocketpowerteam.reallysmartphone.MainApp;

/**
 * Created by gina4_000 on 9/1/2017.
 */
public class BootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent serviceIntent = new Intent(context, MainApp.class);
            context.startService(serviceIntent);
        }
    }
}
