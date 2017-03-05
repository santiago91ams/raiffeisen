package raiffeisen.testapp.generic.dagger;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import raiffeisen.testapp.BuildConfig;
import raiffeisen.testapp.R;
import raiffeisen.testapp.generic.RaiffeisenApi;
import raiffeisen.testapp.generic.RaiffeisenApplication;
import raiffeisen.testapp.helper.Util;
import raiffeisen.testapp.service.WebServiceCall;
import raiffeisen.testapp.ui.homescreen.HomePresenter;
import raiffeisen.testapp.ui.homescreen.HomePresenterImpl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ghita on 05/03/2017.
 */
@Module
public class RaiffeisenModule {

    @Provides
    @Singleton
    Interceptor provideInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                builder.addHeader("Raiffaisen-Android", BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE)
                        .addHeader("Content-Type", "application/json");

                return chain.proceed(builder.build());
            }
        };
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(RaiffeisenApplication.getRes().getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient())
//                .client(client)
                .build();
    }

    @Provides
    @Singleton
    WebServiceCall provideWebService(Retrofit retrofit) {
        return retrofit.create(WebServiceCall.class);
    }


    @Provides
    @Singleton
    RaiffeisenApi provideGuestbookApi() {
        RaiffeisenApi raiffeisenApi = new RaiffeisenApi();
        RaiffeisenApplication.getInstance().component().inject(raiffeisenApi);
        return raiffeisenApi;
    }


    @Provides
    @Singleton
    HomePresenter homePresenter() {
        return new HomePresenterImpl();
    }

    @Provides
    @Singleton
    Util provideUtilState() {
        return new Util();
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}


