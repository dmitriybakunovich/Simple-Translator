package com.example.dima.simple_translator;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private TextView translateText;
    private Button translateButton;

    private final String URL = "https://translate.yandex.net";
    //ключ получаем в личном кабинете яндекса
    private final String KEY = "trnsl.1.1.20170727T191414Z.d9ffadfccb09062a." +
            "0408bc665ca311f353b39eb4ab75751318c91043";

    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    private ILink service = retrofit.create(ILink.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = (EditText) findViewById(R.id.input_text);
        translateText = (TextView) findViewById(R.id.translate_text);
        translateButton = (Button) findViewById(R.id.translate_button);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> mapJson = new HashMap<String, String>();
                mapJson.put("key", KEY);
                mapJson.put("text", inputText.getText().toString());
                mapJson.put("lang", "en-ru");

                Call<Object> call = service.translate(mapJson);
                try {
                    Response<Object> response = call.execute();
                    Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);

                    //вывод только перевод заданного слова
                    for (Map.Entry e : map.entrySet())
                    {
                        if (e.getKey().equals("text")) {
                            //System.out.println(e.getKey() + " " + e.getValue());
                            translateText.setText(" " + e.getValue().toString());
                        }
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                    Toast.makeText(getApplicationContext(),  "Ошибка" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}