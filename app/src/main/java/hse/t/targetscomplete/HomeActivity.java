package hse.t.targetscomplete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    //private static final int LAYOUT = R.layout.activity_home;
    private RecyclerView mRVFishPrice;
    //private RecyclerViewAdapter mAdapter;

    private Button logout_btn;

    private TextView name, email;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
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

        name = findViewById(R.id.name);
        //email = findViewById(R.id.email);
        //logout_btn = findViewById(R.id.logout);

        //Записали сессии авторизированного пользователя
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        //String mEmail = user.get(sessionManager.EMAIL);

        //Установили сессии
        //name.setText(mName);
        //email.setText(mEmail);

        //Кнопка выйти из аккаунта
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });

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

        toolbar.inflateMenu(R.menu.menu);
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
