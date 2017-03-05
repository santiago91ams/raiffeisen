package raiffeisen.testapp.generic;

import android.app.Application;
import android.content.res.Resources;

import raiffeisen.testapp.generic.dagger.DaggerRaiffeisenComponent;
import raiffeisen.testapp.generic.dagger.RaiffeisenComponent;
import raiffeisen.testapp.generic.dagger.RaiffeisenModule;

/**
 * Created by Ghita on 05/03/2017.
 */

public class RaiffeisenApplication extends Application {


    private static RaiffeisenApplication instance;
    private RaiffeisenComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        component = DaggerRaiffeisenComponent.builder()
                .raiffeisenModule(new RaiffeisenModule()).build();
    }

    public RaiffeisenComponent component() {
        return component;
    }

    public static RaiffeisenApplication getInstance() {
        return instance;
    }

    public static Resources getRes() {
        return instance.getResources();
    }


}

