package raiffeisen.testapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ghita on 05/03/2017.
 */

public class UsersListResponse {


    @SerializedName("results")
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}

