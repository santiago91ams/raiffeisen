package raiffeisen.testapp.generic.dagger;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import raiffeisen.testapp.generic.RaiffeisenApi;
import retrofit2.Retrofit;

/**
 * Created by Ghita on 05/03/2017.
 */

public class RaiffeisenModule {

    @Component(modules = RaiffeisenModule.class)
    @Singleton
    public interface RaiffeisenComponent {

        void inject(Interceptor o);
        void inject(OkHttpClient o);
        void inject(Retrofit o);
        void inject(RaiffeisenApi o);
    }
}
