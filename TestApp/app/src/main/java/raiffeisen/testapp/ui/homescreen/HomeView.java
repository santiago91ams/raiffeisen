package raiffeisen.testapp.ui.homescreen;

import raiffeisen.testapp.model.UsersListResponse;

/**
 * Created by Ghita on 05/03/2017.
 */

public interface HomeView {

    void showUsersListFirstPage(UsersListResponse usersListResponse);
    void showUsersListNextPage(UsersListResponse usersListResponse);
    void errorMessage(String errCode);
}

