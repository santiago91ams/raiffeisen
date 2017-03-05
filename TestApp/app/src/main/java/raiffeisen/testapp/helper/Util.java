package raiffeisen.testapp.helper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import raiffeisen.testapp.model.User;
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


        /*
    * Type is for picture type
    *  0 -  thumbnail, medium
    *  1 - large
    *  */
    public void setPicture(Context context, String url, View view, int type) {

        switch (type) {
            case 0:
                Picasso.with(context).load(url).into((CircleImageView) view);
                break;
            case 1:
                Picasso.with(context).load(url).into((ImageView) view);
                break;
            default:
                return;
        }

    }


    /** Determine the space between the first two fingers */
    public float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public String getClearPhoneNumber(String phone) {

        phone = phone.replace("/[^0-9]/g", "");
        return phone;
    }


    public String getAgeLocation(User user){
        String dob = user.getDob();
        String year = dob.substring(0,4);
        String month = dob.substring(5,7);
        String day = dob.substring(8,10);

        Calendar b = Calendar.getInstance();

        b.get(HOUR_OF_DAY);
        b.get(MINUTE);

        int diff = b.get(YEAR) - Integer.valueOf(year);
        if (Integer.valueOf(month) > b.get(MONTH) || ((Integer.valueOf(month) == b.get(MONTH)) && Integer.valueOf(day) > b.get(DATE))) {
            diff--;
        }

        return String.valueOf(diff + " years from " + user.getNat());
    }
}





