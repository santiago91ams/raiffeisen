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
import raiffeisen.testapp.helper.Util;
import raiffeisen.testapp.helper.adapter.UsersListAdapter;
import raiffeisen.testapp.model.User;
import raiffeisen.testapp.model.UsersListResponse;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RaiffeisenApplication) getApplication()).component().inject(this);
        setContentView(R.layout.activity_home_screen);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        presenter.getUsersList(util.getPageNumberToBeLoaded(), this, isFirstPage);


    }

    @Override
    public void showUsersListFirstPage(UsersListResponse usersListResponse) {
        Log.d(TAG, "showUsersList");

        util.setUsersList(usersListResponse);
        isFirstPage = false;

        showRecycleView();
    }


    private void showRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsersListAdapter(this, util.getUsersList(), util);
        adapter.setUserClickListener(this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onDetailsJobClicked(User user) {


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