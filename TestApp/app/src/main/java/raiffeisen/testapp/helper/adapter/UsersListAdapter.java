package raiffeisen.testapp.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import raiffeisen.testapp.R;
import raiffeisen.testapp.helper.Util;
import raiffeisen.testapp.model.User;
import raiffeisen.testapp.model.UsersListResponse;

/**
 * Created by Ghita on 05/03/2017.
 */

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersViewHolder> {

    private UsersListResponse usersList;
    private Context context;
    private Util util;
    private UserClickListener userClick;


    public UsersListAdapter(Context context, UsersListResponse usersList, Util util) {
        this.usersList = usersList;
        this.context = context;
        this.util = util;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_cell, parent, false);

        UsersViewHolder usersViewHolder = new UsersViewHolder(view);

        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        util.setPicture(context, usersList.getUserList().get(position).getPicture().getThumbnail(), holder.profilePic, 0);
        holder.username.setText(WordUtils.capitalize(usersList.getUserList().get(position).getName().getFirst())
                + " " + WordUtils.capitalize(usersList.getUserList().get(position).getName().getLast()));
        holder.ageLocation.setText(WordUtils.capitalize(util.getAgeLocation(usersList.getUserList().get(position))));
        holder.localTime.setText(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
                + Calendar.getInstance().get(Calendar.MINUTE));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userClick != null) {
                    userClick.onDetailsJobClicked(usersList.getUserList().get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.getUserList().size();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_pic)
        CircleImageView profilePic;

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.age_location)
        TextView ageLocation;

        @BindView(R.id.local_time)
        TextView localTime;

        public UsersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setUserClickListener(UserClickListener listener) {
        this.userClick = listener;
    }

    public interface UserClickListener {
        void onDetailsJobClicked(User user);
    }
}

