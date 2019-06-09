package hse.t.targetscomplete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import hse.t.targetscomplete.Dialog.OnSpinerItemClick;
import hse.t.targetscomplete.Dialog.SpinnerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SpinnerDialog spinnerDialog;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRVFishPrice;
    private ImageView mAvatar;

    //private RecyclerViewAdapter mAdapter;

    private TextView mName;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    SessionManager sessionManager;

    Button mAddTarget;

    /*
     * Обновление активити
     * */
    private void Reload() {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAddTarget = (Button)findViewById(R.id.add_target);

        mAddTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this, AddTargetActivity.class);
                startActivity(in);
                Animatoo.animateFade(HomeActivity.this);
            }
        });

        initToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        //toggle.setDrawerIndicatorEnabled(false);
        //toggle.setHomeAsUpIndicator(R.drawable.icnop);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //new AsyncFetch().execute();
            }
        });

        //new AsyncFetch().execute();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        if(sessionManager.getString("MOON") != "1"){
            Reload();
            sessionManager.addString("MOON", "1");
        } else {
            sessionManager.addString("MOON", "0");
        }

        //Записали сессии авторизированного пользователя
        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.NAME);

        View hView = navigationView.getHeaderView(0);
        mName = (TextView)hView.findViewById(R.id.name);
        mAvatar = (ImageView)hView.findViewById(R.id.avatar_profile);
        if(sessionManager.getString("URL").length() < 5){
            mName.setText(name);
            Glide.with(this).load(R.drawable.profile_image).into(mAvatar);
        } else {
            mName.setText(sessionManager.getString("NAME"));
            Glide.with(this).load(sessionManager.getString("URL")).into(mAvatar);
        }

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.actionNewItem:
                Intent intent1 = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;
            case R.id.actionEventsItem:
                Toast.makeText(HomeActivity.this, "Страница событий", Toast.LENGTH_LONG).show();
                break;
            case R.id.actionProfileItem:
                Toast.makeText(HomeActivity.this, "Страница профиля", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                sessionManager.logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
