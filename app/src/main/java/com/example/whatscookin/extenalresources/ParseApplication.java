package com.example.whatscookin.extenalresources;

import android.app.Application;

import com.example.whatscookin.models.Food;
import com.example.whatscookin.Keys;
import com.parse.Parse;
import com.parse.ParseObject;

import java.io.IOException;

/**
 * handles interactions with parse
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse model
        ParseObject.registerSubclass(Food.class);

        Parse.enableLocalDatastore(this);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("enriquemontas-whats-cookin") // should correspond to APP_ID env variable
                .clientKey(Keys.getParseKey())  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://enriquemontas-whats-cookin.herokuapp.com/parse/")
                .enableLocalDataStore().build());
    }

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
