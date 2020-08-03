package com.example.whatscookin;

import android.app.Activity;

import java.io.IOException;

/**
 * general utility methods for other classes to use
 */
public class ActivityUtils extends Activity {

    // simple message to ping a server and check if the user is offline
    public static boolean isOffline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return !(exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return true;
    }
}
