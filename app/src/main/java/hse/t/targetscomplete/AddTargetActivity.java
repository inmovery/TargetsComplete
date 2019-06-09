package hse.t.targetscomplete;

import androidx.appcompat.app.AppCompatActivity;
import ru.yandex.speechkit.Language;
import ru.yandex.speechkit.OnlineModel;
import ru.yandex.speechkit.SpeechKit;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.gui.RecognizerActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AddTargetActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Set<Character> PUNCT_SET = new HashSet<>(Arrays.asList(
            '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-',
            '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^',
            '_', '`', '{', '|', '}', '~'
    ));

    SessionManager sessionManager;

    private static String URL_JOIN = "https://inmovery.ru/app/addtarget.php";

    private static final String API_KEY_FOR_TESTS_ONLY = "069b6659-984b-4c5f-880e-aaedcfd84102";
    private static final int REQUEST_CODE = 31;

    private Vocalizer vocalizer;
    EditText temp;

    EditText name_target;
    EditText detail_target;
    EditText micro_target;


    ImageView mAudioName;
    ImageView mAudioDetail;
    TextView time_end;
    ImageView mMicroTarget;

    Button mAddTarget;

    Target NewTarget;
    ArrayList ArrayOfMicroTarget;
    TextView currentDateTime;
    ListView List;
    Calendar dateAndTime=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_target);

        sessionManager = new SessionManager(this);

        try {
            SpeechKit.getInstance().init(this, API_KEY_FOR_TESTS_ONLY);
            SpeechKit.getInstance().setUuid(UUID.randomUUID().toString());
        } catch (SpeechKit.LibraryInitializationException ignored) {}

        mAudioName = (ImageView)findViewById(R.id.audio_name);
        mAudioDetail = (ImageView)findViewById(R.id.audio_detail);
        time_end = (TextView)findViewById(R.id.time);
        mMicroTarget = (ImageView)findViewById(R.id.audio_micro_target);

        mAudioName.setClickable(true);
        mAudioDetail.setClickable(true);
        mMicroTarget.setClickable(true);

        mAudioName.setOnClickListener(this);
        mAudioDetail.setOnClickListener(this);
        mMicroTarget.setOnClickListener(this);

        mAddTarget = (Button)findViewById(R.id.MakeTarget);

        name_target = (EditText)findViewById(R.id.name_target);
        detail_target = (EditText)findViewById(R.id.detail_target);
        micro_target = (EditText)findViewById(R.id.M);

        mAddTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTargetWithOut();
            }
        });

        currentDateTime=(TextView)findViewById(R.id.time);
        NewTarget = new Target();
        List = (ListView) findViewById(R.id.MicroTarget);
        ArrayOfMicroTarget = new ArrayList(0);
        setInitialDateTime();
    }

    public void AddTargetWithOut(){
        final String name = name_target.getText().toString().trim();
        final String details = detail_target.getText().toString().trim();
        String for_split = "";
        for(int i = 0; i < ArrayOfMicroTarget.size();i++){
            for_split += ArrayOfMicroTarget.get(i);
            if(i != ArrayOfMicroTarget.size() - 1){
                for_split += "|";
            }
        }
        final String micro_targets = for_split.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_JOIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddTargetActivity.this, "Цель добавлена!", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(AddTargetActivity.this, HomeActivity.class);
                        startActivity(in);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddTargetActivity.this, "Ошибка из-за доступа в интернет! "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("details", details);
                params.put("time_end", time_end.getText().toString());
                params.put("for_split", micro_targets);
                params.put("username", sessionManager.getString("EMAIL"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void PickTime(View view) {
        new DatePickerDialog(AddTargetActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    public void AddMicroTarget (View view){
        NewTarget.AddMicroTarget(((EditText)findViewById(R.id.M)).getText().toString());
        ArrayOfMicroTarget.add(((EditText)findViewById(R.id.M)).getText().toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayOfMicroTarget);
        List.setAdapter(adapter);
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {
        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
    }
    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public static String removePunct(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!PUNCT_SET.contains(Character.valueOf(c))) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private void updateTextResult(EditText ex, String message) {
        ex.setText(removePunct(message));
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.audio_name:
                ImageView audio_name = (ImageView) findViewById(R.id.audio_name);
                audio_name.setClickable(true);
                temp = name_target;
                updateTextResult(temp, "");
                Intent intent_name = new Intent(AddTargetActivity.this, RecognizerActivity.class);
                intent_name.putExtra(RecognizerActivity.EXTRA_MODEL, OnlineModel.QUERIES.getName());
                intent_name.putExtra(RecognizerActivity.EXTRA_LANGUAGE, Language.RUSSIAN.getValue());
                startActivityForResult(intent_name, REQUEST_CODE);
                break;
            case R.id.audio_detail:
                ImageView audio_detail = (ImageView) findViewById(R.id.audio_detail);
                audio_detail.setClickable(true);
                temp = detail_target;
                updateTextResult(temp, "");
                Intent intent_detail = new Intent(AddTargetActivity.this, RecognizerActivity.class);
                intent_detail.putExtra(RecognizerActivity.EXTRA_MODEL, OnlineModel.QUERIES.getName());
                intent_detail.putExtra(RecognizerActivity.EXTRA_LANGUAGE, Language.RUSSIAN.getValue());
                startActivityForResult(intent_detail, REQUEST_CODE);
                break;
            case R.id.audio_micro_target:
                ImageView audio_micro_target = (ImageView) findViewById(R.id.audio_micro_target);
                audio_micro_target.setClickable(true);
                temp = micro_target;
                updateTextResult(temp, "");
                Intent intent_micro_target = new Intent(AddTargetActivity.this, RecognizerActivity.class);
                intent_micro_target.putExtra(RecognizerActivity.EXTRA_MODEL, OnlineModel.QUERIES.getName());
                intent_micro_target.putExtra(RecognizerActivity.EXTRA_LANGUAGE, Language.RUSSIAN.getValue());
                startActivityForResult(intent_micro_target, REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RecognizerActivity.RESULT_OK && data != null) {
                final String result = data.getStringExtra(RecognizerActivity.EXTRA_RESULT);
                updateTextResult(temp, removePunct(result));

            } else if (resultCode == RecognizerActivity.RESULT_ERROR) {
                String error = data.getSerializableExtra(RecognizerActivity.EXTRA_ERROR).toString();
                updateTextResult(temp, error);
            }
        }
    }


}