package raiffeisen.testapp.ui.userdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import raiffeisen.testapp.R;
import raiffeisen.testapp.generic.RaiffeisenActivity;
import raiffeisen.testapp.generic.RaiffeisenApplication;
import raiffeisen.testapp.helper.Util;
import raiffeisen.testapp.model.User;

/**
 * Created by Ghita on 05/03/2017.
 */

public class UserDetails extends RaiffeisenActivity {


    @Inject
    Util util;

    @BindView(R.id.profile_pic)
    CircleImageView profilPic;

    @BindView(R.id.username)
    TextView username;

    @BindView(R.id.id)
    TextView id;

    @BindView(R.id.user_details_layout)
    LinearLayout userDetailsLayout;

    private Unbinder unbinder;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RaiffeisenApplication) getApplication()).component().inject(this);
        setContentView(R.layout.activity_user_details);
        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("user");

        user = (User) bundle.get("user");

        initView();
    }

    private void initView() {

        Picasso.with(this).load(user.getPicture().getLarge()).into(profilPic, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
            }
        });


        username.setText(WordUtils.capitalize(user.getName().getFirst()) + " "
                + WordUtils.capitalize(user.getName().getLast()));
        if (!TextUtils.isEmpty(user.getId().getName()) || !TextUtils.isEmpty(user.getId().getValue())) {
            id.setText("ID: " + user.getId().getName() + " " + user.getId().getValue());
        } else id.setVisibility(View.INVISIBLE);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }



}
