package raiffeisen.testapp.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.ConnectivityManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import raiffeisen.testapp.R;
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


    /**
     * Determine the space between the first two fingers
     */
    public float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public String getClearPhoneNumber(String phone) {

        phone = phone.replace("/[^0-9]/g", "");
        return phone;
    }


    public String getAgeLocation(User user) {
        String dob = user.getDob();
        String year = dob.substring(0, 4);
        String month = dob.substring(5, 7);
        String day = dob.substring(8, 10);

        Calendar b = Calendar.getInstance();

        b.get(HOUR_OF_DAY);
        b.get(MINUTE);

        int diff = b.get(YEAR) - Integer.valueOf(year);
        if (Integer.valueOf(month) > b.get(MONTH) || ((Integer.valueOf(month) == b.get(MONTH)) && Integer.valueOf(day) > b.get(DATE))) {
            diff--;
        }

        return String.valueOf(diff + " years from " + user.getNat());
    }


    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void setFlags(User user, ImageView iv, TextView tv, Context context) {

        InputStream ims;
        try {
            ims = context.getAssets().open(user.getNat() + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            iv.setImageDrawable(d);
            iv.setVisibility(View.VISIBLE);

            String phone = user.getPhone();
            if (phone.startsWith("(")) {
                phone = phone.substring(phone.lastIndexOf(')') + 2);
                tv.setText(phone);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getHour() {
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (min < 10 && hour < 10) {
            return "0" + hour + ":" + "0" + min;
        } else if (min < 10) {
            return hour + ":" + "0" + min;
        } else if (hour < 10) {
            return "0" + hour + ":" + min;
        } else return hour + ":" + min;
    }


}





