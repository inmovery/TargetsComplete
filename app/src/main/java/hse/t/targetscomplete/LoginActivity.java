package hse.t.targetscomplete;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private EditText email, password;
    private Button log_btn;
    private TextView link_sign_up;
    private ProgressBar log_loading;
    private static String URL_LOGIN = "https://inmovery.ru/app/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);


        log_loading = findViewById(R.id.log_loading);
        email = findViewById(R.id.log_email);
        password = findViewById(R.id.log_password);
        log_btn = findViewById(R.id.log_btn);
        link_sign_up = findViewById(R.id.link_sign_up);

        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();

                if(!mEmail.isEmpty() || !mPassword.isEmpty()){
                    Login(mEmail, mPassword);
                }else{
                    email.setError("Пожалуйста введите почту");
                    password.setError("Пожалуйста введите пароль");
                }
            }
        });

        link_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        ImageView closeLoginIv = (ImageView) findViewById(R.id.closeLoginIv);
        closeLoginIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.super.onBackPressed();
            }
        });

    }

    private void Login(final String email, final String password){
        log_loading.setVisibility(View.VISIBLE);
        log_btn.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1")){
                                for(int i = 0; i < jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();

                                    sessionManager.createSession(name, email);

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                    finish();
                                    log_loading.setVisibility(View.GONE);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            log_loading.setVisibility(View.GONE);
                            log_btn.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Ошибка! "+e.toString(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(LoginActivity.this, "Ошибка в разборе JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        log_loading.setVisibility(View.GONE);
                        log_btn.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Ошибка в получение JSON", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
