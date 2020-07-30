package com.example.test1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Document doc;
    private TextView editView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editView = findViewById(R.id.editText);
        editView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage(editView);
                    return true;
                }
                return false;
            }
        });

        new MyTask().execute();
        System.out.println(doc);
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                doc = Jsoup.connect("https://www.worldometers.info/coronavirus/#countries").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

        /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        String[] outs = searchSite(editView.getText().toString());

        if (outs == null) {
            Toast.makeText(this.getApplicationContext(), "Could not find country/continent", Toast.LENGTH_LONG).show();
            editView.setText("");
        } else {
            ViewGroup vg = (ViewGroup)findViewById(R.id.layout);
            for (int i = 0; i < vg.getChildCount(); i++) {
                TextView tv = (TextView) vg.getChildAt(i);
                tv.setText(getResources().getStringArray(R.array.labels)[i] + outs[i]);
            }
        }
            //        Intent intent = new Intent(this, DisplayMessageActivity.class)
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
    }

    private String[] searchSite(String country) {
        Element table = doc.select("table#main_table_countries_today").get(0);
        Element rows = table.select("tbody").get(0);

        for (Element row : rows.select("tr")) {

            String rowCountry = row.select("td").get(1).text();
            if (country.trim().equalsIgnoreCase(rowCountry)) {


                for (Element r : row.select("td"))
                    System.out.println(r.text()+"\n");

                String[] out = new String[6];
                out[0] = " "+row.select("td").get(2).text();
                out[1] = " "+row.select("td").get(8).text();
                out[2] = " "+row.select("td").get(3).text();
                out[3] = " "+row.select("td").get(4).text();
                out[4] = " "+row.select("td").get(5).text();
                out[5] = " "+row.select("td").get(6).text();
                return out;
            }

        }
        return null;
    }
}