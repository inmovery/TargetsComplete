package hse.t.targetscomplete;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
