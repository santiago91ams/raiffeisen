package raiffeisen.testapp.ui.userdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Ghita on 05/03/2017.
 */

public class UserDetails extends RaiffeisenActivity {


    @Inject
    Util util;

    @BindView(R.id.profile_pic)
    CircleImageView profilPic;

    @BindView(R.id.pinch_pic)
    PhotoView pinchPhoto;

    @BindView(R.id.pinch_pic_frame)
    FrameLayout pinchFrame;

    @BindView(R.id.username)
    TextView username;

    @BindView(R.id.id)
    TextView id;

    @BindView(R.id.user_details_layout)
    LinearLayout userDetailsLayout;

    private String TAG = "UserDetails";
    private Unbinder unbinder;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RaiffeisenApplication) getApplication()).component().inject(this);
        setContentView(R.layout.activity_user_details);
        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(getString(R.string.user));

        user = (User) bundle.get(getString(R.string.user));

        initView();
    }

    private void initView() {

        Picasso.with(this).load(user.getPicture().getLarge()).into(profilPic, new Callback() {
            @Override
            public void onSuccess() {
                profilPic.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_POINTER_DOWN:
                                if (util.spacing(event) > 50f) {
                                    goPinch();
                                }

                                break;
                        }
                        return true;
                    }
                });
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

        for (int i = 0; i < 3; i++) {
            userDetailsLayout.addView(getUserTabDetails(i));
        }

    }

    /*
  * type of contact field
  * 0 - phone
  * 1 - email
  * 2 - address
  * easy to add more*/
    private View getUserTabDetails(int type) {

        View tabDetails = LayoutInflater.from(this).inflate(R.layout.user_contact_tab, userDetailsLayout, false);
        TextView contactTypeValueTxt = ButterKnife.findById(tabDetails, R.id.icon_contact_type_value);
        TextView contactTypeTxt = ButterKnife.findById(tabDetails, R.id.icon_contact_type);
        ImageView contactPic = ButterKnife.findById(tabDetails, R.id.icon_pic);
        ImageView flag = ButterKnife.findById(tabDetails, R.id.flag);

        switch (type) {
            case 0:
                contactPic.setImageDrawable(getResources().getDrawable(R.drawable.ic_phone_black_24dp));
                contactTypeTxt.setText("Phone");
                contactTypeValueTxt.setText(user.getPhone());
                util.setFlags(user, flag, contactTypeValueTxt,this);
                tabDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + util.getClearPhoneNumber(user.getPhone())));
                        startActivity(intent);
                    }
                });

                break;
            case 1:
                contactPic.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp));
                contactTypeTxt.setText("Email");
                contactTypeValueTxt.setText(user.getEmail());
                tabDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
                        startActivity(Intent.createChooser(intent, ""));
                    }
                });
                break;
            case 2:
                contactPic.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_location_black_24dp));
                contactTypeTxt.setText("Address");
                contactTypeValueTxt.setText(WordUtils.capitalize(user.getLocation().getCity()) + ", " +
                        (user.getLocation().getStreet()));
                tabDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String map = "http://maps.google.co.in/maps?q=" + user.getLocation().getStreet();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                        startActivity(i);
                    }
                });
                break;
            default:
        }

        return tabDetails;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    void goPinch() {

        final PhotoViewAttacher attacher = new PhotoViewAttacher(pinchPhoto);
        Picasso.with(this).load(user.getPicture().getLarge()).into(pinchPhoto, new Callback() {
            @Override
            public void onSuccess() {
                attacher.update();
                pinchFrame.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                Log.d(TAG, "picasso photoviewattacher error");
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(pinchFrame.getVisibility()==View.VISIBLE){
            pinchFrame.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

}
