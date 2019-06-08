package hse.t.targetscomplete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        Log.d("LOGIN_IS_TRUE", String.valueOf(sessionManager.isLoggin()));
        final Intent ir;
        if(sessionManager.isLoggin() == true){
            ir = new Intent(this, HomeActivity.class);
        } else {
            ir = new Intent(this, MainActivity.class);
        }

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(ir);
                    Animatoo.animateFade(SplashActivity.this);
                    finish();
                }
            }
        };
        timer.start();

    }
}
