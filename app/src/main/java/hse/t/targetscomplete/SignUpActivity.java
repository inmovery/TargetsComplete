package hse.t.targetscomplete;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, password, reset_password;
    private Button reg_btn;
    private ProgressBar reg_loading;
    private static String URL_JOIN = "https://inmovery.ru/app/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        reg_loading = findViewById(R.id.reg_loading);
        name = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        reset_password = findViewById(R.id.reg_reset_password);
        reg_btn = findViewById(R.id.reg_btn);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        ImageView closeSignUpIv = (ImageView) findViewById(R.id.closeSignUpIv);
        closeSignUpIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.super.onBackPressed();
            }
        });

    }

    private void SignUp(){
        reg_loading.setVisibility(View.VISIBLE);
        reg_btn.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_JOIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(SignUpActivity.this, "Аккаунт создан!", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(in);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUpActivity.this, "Ошибка! "+e.toString(), Toast.LENGTH_SHORT).show();
                            reg_loading.setVisibility(View.GONE);
                            reg_btn.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpActivity.this, "Ошибка! "+error.toString(), Toast.LENGTH_SHORT).show();
                        reg_loading.setVisibility(View.GONE);
                        reg_btn.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
