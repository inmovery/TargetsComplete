package hse.t.targetscomplete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
//        Log.d("SPLASH_VK", String.valueOf(Arrays.asList(fingerprints)));

        sessionManager = new SessionManager(this);
        
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
