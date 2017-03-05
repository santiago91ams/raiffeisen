package raiffeisen.testapp.helper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;

import raiffeisen.testapp.model.UsersListResponse;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by Ghita on 05/03/2017.
 */

public class Util {

    //    counter for the user list page to be loaded
    private static int pageNoToBeLoaded = 0;
    private UsersListResponse usersList;


    public String getPageNumberToBeLoaded() {
        return String.valueOf(pageNoToBeLoaded++);
    }


    public void setUsersList(UsersListResponse usersListPage) {
        if (usersList != null) {
            usersList.getUserList().addAll(usersListPage.getUserList());
        } else {
            usersList = usersListPage;
        }

    }

    public UsersListResponse getUsersList() {
        return usersList;
    }

}





