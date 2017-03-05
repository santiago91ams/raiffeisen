package raiffeisen.testapp.generic;

import android.util.Log;

import javax.inject.Inject;

import raiffeisen.testapp.model.UsersListResponse;
import raiffeisen.testapp.service.WebServiceCall;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ghita on 05/03/2017.
 */

public class RaiffeisenApi {

    @Inject
    Retrofit retrofit;
    @Inject
    WebServiceCall ws;

    private String TAG = "RaiffeisenApi";

    public interface Callback<R> {
        void onSuccess(R result);

        void onFailure(String code);
    }

    public void getUsers(final Callback<UsersListResponse> callback, String page) {
        ws.getUsers(page).enqueue(new retrofit2.Callback<UsersListResponse>() {
            @Override
            public void onResponse(Call<UsersListResponse> call, Response<UsersListResponse> response) {
                UsersListResponse usersListResponse = response.body();
                callback.onSuccess(usersListResponse);
            }

            @Override
            public void onFailure(Call<UsersListResponse> call, Throwable t) {
                Log.d(TAG,"failure");
            }
        });
    }

}
