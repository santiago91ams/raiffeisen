package raiffeisen.testapp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import raiffeisen.testapp.ui.homescreen.HomeScreen;

/**
 * Created by Ghita on 05/03/2017.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    HomeScreen homeScreen;

    public ConnectivityChangeReceiver(HomeScreen homeScreen){
        this.homeScreen = homeScreen;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("*** Action: " + intent.getAction());
        if(intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")) {
            homeScreen.internetConnection();
        }
    }


}