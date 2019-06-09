package hse.t.targetscomplete;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKServiceActivity;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Vk_Auth extends AppCompatActivity  implements View.OnClickListener {

    private static String URL_JOIN = "https://inmovery.ru/app/register.php";

    private String[] scope = new String[]{VKScope.WALL,VKScope.EMAIL};

    SessionManager sessionManager;
    int UserId;// ID пользователя

    /*
     * Получение ID пользователя
     * */
    int getMyId() {
        final VKAccessToken idToken = VKAccessToken.currentToken();
        return idToken != null ? Integer.parseInt(idToken.userId) : 0;
    }

    private void SignUp(final String name, final String email, final String password, final String url){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_JOIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(Vk_Auth.this, "Аккаунт создан!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Vk_Auth.this, "Ошибка! "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Vk_Auth.this, "Ошибка! "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("url", url);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk__auth);

        sessionManager = new SessionManager(this);

        VKSdk.login(this, scope);
        UserId = getMyId();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(Vk_Auth.this, HomeActivity.class);

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.OWNER_ID,"UserID", VKApiConst.FIELDS,"first_name, photo_200"));

                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        String n = response.responseString;
                        Log.d("ololo", n);
                        try {
                            JSONObject jsonObject = new JSONObject(n);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject object = jsonArray.getJSONObject(0);
                            String name = object.getString("last_name") + " " + object.getString("first_name");
                            String url = object.getString("photo_200");
                            String id = String.valueOf(object.getInt("id"));
                            sessionManager.addString("URL", url);
                            sessionManager.addString("EMAIL", id);
                            sessionManager.addString("NAME", name);
                            sessionManager.setLoginBoolean("IS_LOGIN", true);

                            SignUp(name, id, "qwer1234", url);

                            //mName.setText(name);
                            //Glide.with(getApplicationContext()).load(url).into(mAvatar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // Пользователь успешно авторизовался
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        sessionManager.setLoginBoolean("IS_LOGIN", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("name", sessionManager.getString("NAME"));
        intent.putExtra("url", sessionManager.getString("URL"));
        intent.putExtra("email", sessionManager.getString("EMAIL"));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vk_btn:
                Toast.makeText(getApplicationContext(), "Всё работает!", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }
}
