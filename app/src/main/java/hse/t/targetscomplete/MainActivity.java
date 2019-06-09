package hse.t.targetscomplete;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vk.sdk.VKServiceActivity;

public class MainActivity extends AppCompatActivity {

    ImageView Vk_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vk_Btn = (ImageView)findViewById(R.id.vk_btn);

        Vk_Btn.setClickable(true);
        Vk_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vk_Btn.setClickable(true);
                Intent resultIntent = new Intent(MainActivity.this, Vk_Auth.class);
                startActivity(resultIntent);
                finish();
            }
        });

    }

    public void JoinPressed(View view){
        //Создание перехода на страницу регистрации
        Intent signupIntent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(signupIntent);
    }
    public void LoginPressed(View view){
        //Создание перехода на страницу входа
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

}
