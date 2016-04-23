package mrpo.balaaagi.me.mrpo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by balaaagi on 23/04/16.
 */
public class MyReceivar extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);
    }
}
