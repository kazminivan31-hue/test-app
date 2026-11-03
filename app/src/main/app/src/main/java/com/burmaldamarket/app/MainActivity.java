package com.burmaldamarket.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView appsList;
    ArrayList<String> appNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        appsList = new ListView(this);
        setContentView(appsList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://github.io");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        json.append(line);
                    }
                    
                    JSONArray array = new JSONArray(json.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        appNames.add(obj.getString("title") + " (v" + obj.getString("version") + ")");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, appNames);
                            appsList.setAdapter(adapter);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Ошибка загрузки BurmaldaMarket!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
}
