package raiffeisen.testapp.service;

import raiffeisen.testapp.model.UsersListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ghita on 05/03/2017.
 */

public interface WebServiceCall {

    @GET("?&seed=abc&results=100")
    Call<UsersListResponse> getUsers(@Query("page") String page);

}
