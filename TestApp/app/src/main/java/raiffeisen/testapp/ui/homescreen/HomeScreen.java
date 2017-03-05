package raiffeisen.testapp.ui.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import raiffeisen.testapp.R;
import raiffeisen.testapp.generic.RaiffeisenActivity;
import raiffeisen.testapp.generic.RaiffeisenApplication;
import raiffeisen.testapp.helper.EndlessRecyclerViewScrollListener;
import raiffeisen.testapp.helper.Util;
import raiffeisen.testapp.helper.adapter.UsersListAdapter;
import raiffeisen.testapp.model.User;
import raiffeisen.testapp.model.UsersListResponse;
import raiffeisen.testapp.ui.userdetails.UserDetails;

public class HomeScreen extends RaiffeisenActivity implements HomeView, UsersListAdapter.UserClickListener {

    @Inject
    HomePresenter presenter;
    @Inject
    Util util;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.users_recycle_view)
    RecyclerView recyclerView;

    private String TAG = "HomeScreen";
    private Unbinder unbinder;
    private boolean isFirstPage = true;
    private UsersListAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RaiffeisenApplication) getApplication()).component().inject(this);
        setContentView(R.layout.activity_home_screen);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        // if the activity is destroyed from stack, don't make another call for list
        if(util.getUsersList()==null){
            presenter.getUsersList(util.getPageNumberToBeLoaded(), this, isFirstPage);
        } else {
            showRecycleView();
        }


    }

    @Override
    public void showUsersListFirstPage(UsersListResponse usersListResponse) {
        Log.d(TAG, "showUsersList");

        util.setUsersList(usersListResponse);
        isFirstPage = false;

        showRecycleView();
    }

    @Override
    public void showUsersListNextPage(UsersListResponse usersListResponse) {
        util.setUsersList(usersListResponse);
        adapter.notifyItemRangeInserted(adapter.getItemCount(), util.getUsersList().getUserList().size() - 1);
        scrollListener.resetState();
    }


    private void showRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsersListAdapter(this, util.getUsersList(), util);
        adapter.setUserClickListener(this);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataPage(util.getPageNumberToBeLoaded());
            }

        };

        recyclerView.addOnScrollListener(scrollListener);
    }


    @Override
    public void onDetailsJobClicked(User user) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);

        Intent intent = new Intent(HomeScreen.this, UserDetails.class);
        intent.putExtra("user", bundle);
        startActivity(intent);

    }


    private void loadNextDataPage(String page) {
        presenter.getUsersList(page, this, isFirstPage);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick(R.id.fab)
    public void onFabClick() {
        Toast.makeText(this, "I am a floating button!", Toast.LENGTH_SHORT).show();
    }

}
