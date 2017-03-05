package raiffeisen.testapp.ui.homescreen;

import android.content.Intent;
import android.content.IntentFilter;
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
import raiffeisen.testapp.helper.ConnectivityChangeReceiver;
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
    private ConnectivityChangeReceiver connectivityChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RaiffeisenApplication) getApplication()).component().inject(this);
        setContentView(R.layout.activity_home_screen);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        connectivityChangeReceiver = new ConnectivityChangeReceiver(this);

        // check for internet, if no internet and user list is empty, register listener
        // to do call when has connection
        if (util.getUsersList() == null && util.isNetworkConnected(this)) {
            presenter.getUsersList(util.getPageNumberToBeLoaded(), this, isFirstPage);

        } else if (util.getUsersList() == null && !util.isNetworkConnected(this)) {
            final IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            super.registerReceiver(connectivityChangeReceiver, filter);
            Toast.makeText(this, getString(R.string.internet_connect), Toast.LENGTH_SHORT).show();

        } else {
            showRecycleView();
        }


    }

    @Override
    public void showUsersListFirstPage(UsersListResponse usersListResponse) {
        Log.d(TAG, "showUsersList");

        util.setUsersList(usersListResponse);
        isFirstPage = false;
        if (isInForeground)
            showRecycleView();
    }

    @Override
    public void showUsersListNextPage(UsersListResponse usersListResponse) {
        util.setUsersList(usersListResponse);
        if (isInForeground) {
            adapter.notifyItemRangeInserted(adapter.getItemCount(), util.getUsersList().getUserList().size() - 1);
            scrollListener.resetState();
        }

    }

    @Override
    public void errorMessage(String errCode) {
        if(isInForeground)
            Toast.makeText(this,getResources().getString(R.string.error_message) + errCode, Toast.LENGTH_SHORT).show();
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
        bundle.putParcelable(getString(R.string.user), user);

        Intent intent = new Intent(HomeScreen.this, UserDetails.class);
        intent.putExtra(getString(R.string.user), bundle);
        startActivity(intent);

    }


    private void loadNextDataPage(String page) {
        presenter.getUsersList(page, this, isFirstPage);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick(R.id.fab)
    public void onFabClick() {
        Toast.makeText(this, getString(R.string.floating_button_click), Toast.LENGTH_SHORT).show();
    }

    public void internetConnection() {

        if (util.getUsersList() == null && util.isNetworkConnected(this)) {
            presenter.getUsersList(util.getPageNumberToBeLoaded(), this, isFirstPage);
        }

    }

}
