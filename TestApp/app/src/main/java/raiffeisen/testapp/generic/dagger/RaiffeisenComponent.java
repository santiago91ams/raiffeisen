package raiffeisen.testapp.generic.dagger;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import raiffeisen.testapp.generic.RaiffeisenApi;
import raiffeisen.testapp.helper.Util;
import raiffeisen.testapp.service.WebServiceCall;
import raiffeisen.testapp.ui.homescreen.HomePresenterImpl;
import raiffeisen.testapp.ui.homescreen.HomeScreen;
import raiffeisen.testapp.ui.userdetails.UserDetails;
import retrofit2.Retrofit;

/**
 * Created by Ghita on 05/03/2017.
 */

@Component(modules = RaiffeisenModule.class)
@Singleton
public interface RaiffeisenComponent {

    void inject(Interceptor o);
    void inject(OkHttpClient o);
    void inject(Retrofit o);
    void inject(WebServiceCall o);
    void inject(RaiffeisenApi o);
    void inject(HomeScreen o);
    void inject(HomePresenterImpl o);
    void inject(Util o);
    void inject(UserDetails o);

}
