package raiffeisen.testapp.ui.homescreen;

import android.util.Log;

import javax.inject.Inject;

import raiffeisen.testapp.generic.RaiffeisenApi;
import raiffeisen.testapp.generic.RaiffeisenApplication;
import raiffeisen.testapp.model.UsersListResponse;

/**
 * Created by Ghita on 05/03/2017.
 */

public class HomePresenterImpl implements HomePresenter {

    private String TAG = "HomPresenterImpl";

    @Inject
    RaiffeisenApi api;

    public HomePresenterImpl() {
        RaiffeisenApplication.getInstance().component().inject(this);
    }

    @Override
    public void getUsersList(String page, final HomeView view, final boolean isFirstPage) {
        api.getUsers(new RaiffeisenApi.Callback<UsersListResponse>() {
            @Override
            public void onSuccess(UsersListResponse result) {
                Log.d(TAG, "succes");
                if(isFirstPage){
                    view.showUsersListFirstPage(result);
                } else {
                    view.showUsersListNextPage(result);
                }

            }

            @Override
            public void onFailure(String code) {
                Log.d(TAG, "failed");
                view.errorMessage(code);
            }
        }, page);
    }
}

